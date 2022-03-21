package ch.bbw.pg.olat.data.generator;

import ch.bbw.pg.olat.data.entity.Course;
import ch.bbw.pg.olat.data.entity.User;
import ch.bbw.pg.olat.data.enums.Role;
import ch.bbw.pg.olat.data.repository.CourseRepository;
import ch.bbw.pg.olat.data.repository.UserRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, UserRepository userRepository, CourseRepository courseRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }

            logger.info("Generating demo data");

            logger.info("... generating 1 User entities...");
            User admin = new User();
            admin.setFirstname("Admin");
            admin.setLastname("Admin");
            admin.setRole(Role.ADMIN);
            admin.setUsername("admin.admin");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setEmail("gatzka@sowatec.com");
            userRepository.save(admin);

            User student = new User();
            student.setFirstname("Philipp");
            student.setLastname("Gatzka");
            student.setEmail("phil.gatzka@gmail.com");
            student.setRole(Role.STUDENT);
            student.setUsername("philipp.gatzka");
            student.setHashedPassword(passwordEncoder.encode("gatzka"));
            userRepository.save(student);

            User teacher = new User();
            teacher.setFirstname("Peter");
            teacher.setLastname("Rutschmann");
            teacher.setUsername("Peter.Rutschmann");
            teacher.setRole(Role.TEACHER);
            teacher.setHashedPassword(passwordEncoder.encode("teacher"));
            teacher.setEmail("rutschmann@gmail.com");
            userRepository.save(teacher);

            Course course = new Course();
            course.setName("5IA19a");
            course.setTeacher(teacher);
            course.addStudent(student);
            courseRepository.save(course);


            logger.info("Generated demo data");
        };
    }

}