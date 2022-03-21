package ch.bbw.pg.olat.views.personform;


import ch.bbw.pg.olat.data.enums.Role;
import ch.bbw.pg.olat.data.service.UserService;
import ch.bbw.pg.olat.views.MainLayout;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.security.RolesAllowed;

@PageTitle("Register User")
@Route(value = "register-user", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed({"ADMIN", "TEACHER"})
@Uses(Icon.class)
public class UserRegisterView extends Div {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final TextField inputFirstname = new TextField("Firstname");
    private final TextField inputLastname = new TextField("Lastname");
    private final TextField inputUsername = new TextField("Username");
    private final EmailField inputEmail = new EmailField("Email");
    private final Select<Role> inputRole = new Select<>(Role.values());
    private final PasswordField inputPassword = new PasswordField("Password");
    private final PasswordField inputConfirmPassword = new PasswordField("Confirm Password");

    private final Button buttonCancel = new Button("Cancel");
    private final Button buttonSave = new Button("Save");

    private final Binder<UserRegisterHolder> binder = new Binder<>(UserRegisterHolder.class);

    public UserRegisterView(UserService userService, PasswordEncoder passwordEncoder) {
        addClassName("user-register-form");
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        bindInstanceFields();
        clearForm();

        buttonCancel.addClickListener(e -> clearForm());

        buttonSave.addClickListener(e -> saveBean());

    }

    private void bindInstanceFields() {
        binder.forField(inputFirstname).bind(UserRegisterHolder::getFirstname, UserRegisterHolder::setFirstname);
        binder.forField(inputLastname).bind(UserRegisterHolder::getLastname, UserRegisterHolder::setLastname);
        binder.forField(inputUsername).bind(UserRegisterHolder::getUsername, UserRegisterHolder::setUsername);
        binder.forField(inputEmail).bind(UserRegisterHolder::getEmail, UserRegisterHolder::setEmail);
        binder.forField(inputRole).bind(UserRegisterHolder::getRole, UserRegisterHolder::setRole);
        binder.forField(inputPassword).bind(UserRegisterHolder::getPassword, UserRegisterHolder::setPassword);
        binder.forField(inputConfirmPassword).bind(UserRegisterHolder::getConfirmPassword, UserRegisterHolder::setConfirmPassword);
    }

    private void saveBean() {
        String message;
        if ((message = binder.getBean().validate(passwordEncoder)).equals("")) {
            userService.save(binder.getBean().convert());
            Notification.show(binder.getBean().getClass().getSimpleName() + " details stored.");
            clearForm();
        } else {
            Notification.show(message);
        }
    }

    private void clearForm() {
        binder.setBean(new UserRegisterHolder());
    }


    private Component createTitle() {
        return new H3("User registration");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        inputEmail.setErrorMessage("Please enter a valid email address");
        inputFirstname.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>) textFieldStringComponentValueChangeEvent -> inputUsername.setValue(inputFirstname.getValue() + "." + inputLastname.getValue()));
        inputLastname.addValueChangeListener((HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>) textFieldStringComponentValueChangeEvent -> inputUsername.setValue(inputFirstname.getValue() + "." + inputLastname.getValue()));
        formLayout.add(inputFirstname, inputLastname, inputUsername, inputRole, inputEmail, inputPassword, inputConfirmPassword);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        buttonSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(buttonSave);
        buttonLayout.add(buttonCancel);
        return buttonLayout;
    }
}
