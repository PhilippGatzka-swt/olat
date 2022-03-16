package ch.bbw.pg.olat.data.service;

import ch.bbw.pg.olat.data.entity.SamplePerson;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SamplePersonRepository extends JpaRepository<SamplePerson, UUID> {

}