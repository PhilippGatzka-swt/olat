package ch.bbw.pg.olat.views.course.detail;

import ch.bbw.pg.olat.data.entity.Course;
import ch.bbw.pg.olat.data.entity.User;
import ch.bbw.pg.olat.data.enums.Role;
import ch.bbw.pg.olat.data.service.CourseService;
import ch.bbw.pg.olat.data.service.UserService;
import ch.bbw.pg.olat.views.MainLayout;
import ch.bbw.pg.olat.views.util.FormView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@PageTitle("Course Detail")
@Route(value = "detail-course", layout = MainLayout.class)
@RolesAllowed({"ADMIN", "TEACHER"})
@Uses(Icon.class)
public class CourseDetailView extends Div implements BeforeEnterObserver, FormView {

    private final UserService userService;
    private final CourseService courseService;

    private final Select<User> inputStudent = new Select<>();
    private final Select<Course> inputCourse = new Select<>();

    private final Grid<User> grid = new Grid<>(User.class, false);

    private final Button buttonSave = new Button("Add");

    public CourseDetailView(UserService userService, CourseService courseService) {
        addClassName("course-detail-view");
        this.userService = userService;
        this.courseService = courseService;

        SplitLayout layout = new SplitLayout();

        layout.setOrientation(SplitLayout.Orientation.VERTICAL);

        createFormLayout(layout);
        createGridLayout(layout);

        add(layout);
    }

    private void createGridLayout(SplitLayout layout) {
        grid.addColumn("firstname");
        grid.addColumn("lastname");
        grid.addColumn("email");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        layout.addToSecondary(grid);
    }

    private void refreshGrid() {
        grid.getListDataView().refreshAll();
    }

    private void createFormLayout(SplitLayout layout) {
        inputCourse.setLabel("Student");
        inputCourse.setItems(courseService.findAll());
        inputCourse.setItemLabelGenerator((ItemLabelGenerator<Course>) Course::getName);

        inputCourse.addValueChangeListener(event -> {
           grid.setItems(courseService.get(inputCourse.getValue().getId()).get().getStudents());
        });


        inputStudent.setLabel("Course");
        List<User> studentList = userService.findAllByRole(Role.STUDENT);
        inputStudent.setItems(studentList);
        inputStudent.setItemLabelGenerator((ItemLabelGenerator<User>) user -> user.getFirstname() + " " + user.getLastname());

        Div formLayout = new Div(new FormLayout(inputCourse, inputStudent));
        createButtonLayout(formLayout);
        layout.addToPrimary(formLayout);
    }

    private void createButtonLayout(Div formLayout) {
        buttonSave.addClickListener(this::save);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setClassName("button-layout");

        buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        horizontalLayout.add(buttonSave);
        formLayout.add(horizontalLayout);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        //   Optional<Long> courseId = event.getRouteParameters().get(COURSE_ID).map(Long::parseLong);
        //   if (courseId.isPresent()) {
        //       Optional<Course> course = courseService.get(courseId.get());
        //       if (course.isPresent()) {
        //           populateForm(course.get());
        //       } else {
        //           Notification.show(
        //                   String.format("The requested course was not found, ID = %s", courseId.get()), 3000,
        //                   Notification.Position.BOTTOM_START);
        //           refreshGrid();
        //           event.forwardTo(ch.bbw.pg.olat.views.course.master.CourseMasterView.class);
        //       }
        //   }
    }

    @Override
    public void save(ClickEvent<Button> clickEvent) {
        Course course = inputCourse.getValue();
        course.addStudent(inputStudent.getValue());
        courseService.save(course);
    }

}