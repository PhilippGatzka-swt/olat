package ch.bbw.pg.olat.data.service;

import ch.bbw.pg.olat.data.entity.Course;
import ch.bbw.pg.olat.data.entity.User;
import ch.bbw.pg.olat.data.repository.CourseRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseService extends AbstractService<Course, CourseRepository> {
    public CourseService(CourseRepository repository) {
        super(repository);
    }



}
