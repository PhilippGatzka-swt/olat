package ch.bbw.pg.olat.views.personform;

import ch.bbw.pg.olat.data.entity.User;
import ch.bbw.pg.olat.data.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRegisterHolder {

    private String firstname, lastname, username, password, email, confirmPassword, hashedPassword;
    private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String validate(PasswordEncoder passwordEncoder){
        if (username.equals("")) return "Username is a required value!";
        if (firstname.equals("")) return "Firstname is a required value!";
        if (lastname.equals("")) return "Lastname is a required value!";
        if (email.equals("")) return "Email is a required value!";
        if (password.equals("")) return "Password is a required value!";
        if (confirmPassword.equals("")) return "Confirm Password is a required value!";
        if (role == null) return "Role is a required value!";
        if (!password.equals(confirmPassword)) return "Passwords do not match!";
        hashedPassword = passwordEncoder.encode(password);
        return "";
    }

    public User convert() {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setUsername(username);
        user.setRole(role);
        user.setHashedPassword(hashedPassword);
        return user;
    }
}
