package ch.bbw.pg.olat.data.repository;

import ch.bbw.pg.olat.data.entity.User;
import ch.bbw.pg.olat.data.enums.Role;

import java.util.List;

public interface UserRepository extends AbstractRepository<User> {

    User findByUsername(String username);

    List<User> findAllByRole(Role role);

}
