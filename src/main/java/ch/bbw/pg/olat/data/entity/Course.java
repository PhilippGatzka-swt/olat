package ch.bbw.pg.olat.data.entity;

import ch.bbw.pg.olat.data.enums.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "course")
public class Course extends AbstractEntity {
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "course_users",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    private Collection<User> students = new ArrayList<>();

    public Collection<User> getStudents() {
        return students;
    }

    public void setStudents(Collection<User> users) {
        this.students = users;
    }

    public void addStudent(User student){
        if (student.getRole() == Role.STUDENT){
            students.add(student);
        }else{
            throw new IllegalArgumentException("User does not have 'Student' role!");
        }
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}