package ch.bbw.pg.olat.data.service;

import ch.bbw.pg.olat.data.entity.User;
import ch.bbw.pg.olat.data.enums.Role;
import ch.bbw.pg.olat.data.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends AbstractService<User, UserRepository> {
    public UserService(UserRepository repository) {
        super(repository);
    }

    public List<User> findAllByRole(Role role) {
        return repository_.findAllByRole(role);
    }
}
