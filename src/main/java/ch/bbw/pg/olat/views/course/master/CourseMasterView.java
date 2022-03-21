package ch.bbw.pg.olat.views.course.master;

import ch.bbw.pg.olat.data.entity.Course;
import ch.bbw.pg.olat.data.entity.User;
import ch.bbw.pg.olat.data.enums.Role;
import ch.bbw.pg.olat.data.service.CourseService;
import ch.bbw.pg.olat.data.service.UserService;
import ch.bbw.pg.olat.views.MainLayout;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

@PageTitle("Course Master")
@Route(value = "master-course/:courseId?/:action?(edit)", layout = MainLayout.class)
//@Route(value = "master-course", layout = MainLayout.class)
@RolesAllowed({"ADMIN", "TEACHER"})
@Uses(Icon.class)
public class CourseMasterView extends Div implements BeforeEnterObserver {

    private final String COURSE_ID = "courseId";
    private final String COURSE_EDIT_ROUTE_TEMPLATE = "master-course/%s/edit";

    private final UserService userService;
    private final CourseService courseService;

    private final TextField inputCoursename = new TextField("Course name");
    private final Select<User> inputTeacher = new Select<>();

    private final Grid<Course> grid = new Grid<>(Course.class, false);

    private final Button buttonCancel = new Button("Cancel");
    private final Button buttonSave = new Button("Save");

    private Course course;

    private final BeanValidationBinder<Course> binder = new BeanValidationBinder<>(Course.class);

    public CourseMasterView(UserService userService, CourseService courseService) {
        addClassName("master-course-view");
        this.userService = userService;
        this.courseService = courseService;

        SplitLayout layout = new SplitLayout();

        layout.setOrientation(SplitLayout.Orientation.VERTICAL);

        createFormLayout(layout);
        createGridLayout(layout);

        add(layout);

    }

    private void createGridLayout(SplitLayout layout) {
        grid.addColumn("name");
        grid.addColumn("teacher");
        grid.setItems(courseService.findAll());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(COURSE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(CourseMasterView.class);
            }
        });
        layout.addToSecondary(grid);
        bindInstanceFields();
    }

    private void bindInstanceFields() {
        binder.bind(inputCoursename, Course::getName, Course::setName);
        binder.bind(inputTeacher, Course::getTeacher, Course::setTeacher);
    }

    private void save() {
        try {
            if (this.course == null) {
                this.course = new Course();
            }
            binder.writeBean(this.course);

            courseService.save(this.course);
            clearForm();
            refreshGrid();
            Notification.show("Course details stored.");
            UI.getCurrent().navigate(CourseMasterView.class);
        } catch (ValidationException validationException) {
            Notification.show("An exception happened while trying to store the course details.");
        }
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getListDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void createFormLayout(SplitLayout layout) {
        inputTeacher.setItems(userService.findAllByRole(Role.TEACHER));
        inputTeacher.setItemLabelGenerator((ItemLabelGenerator<User>) user -> user.getFirstname() + " " + user.getLastname());
        Div formLayout = new Div(new FormLayout(inputCoursename, inputTeacher));
        createButtonLayout(formLayout);
        layout.addToPrimary(formLayout);
    }

    private void createButtonLayout(Div formLayout) {
        buttonCancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        buttonSave.addClickListener(e -> {
            save();
        });
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setClassName("button-layout");
        buttonCancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        horizontalLayout.add(buttonSave, buttonCancel);
        formLayout.add(horizontalLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> courseId = event.getRouteParameters().get(COURSE_ID).map(Long::parseLong);
        if (courseId.isPresent()) {
            Optional<Course> course = courseService.get(courseId.get());
            if (course.isPresent()) {
                populateForm(course.get());
            } else {
                Notification.show(
                        String.format("The requested course was not found, ID = %s", courseId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                refreshGrid();
                event.forwardTo(CourseMasterView.class);
            }
        }
    }

    private void populateForm(Course course) {
        this.course = course;
        if (course != null)
            inputTeacher.setValue(course.getTeacher());
        binder.readBean(this.course);
    }

}
