package ch.bbw.pg.olat.data.service;

import ch.bbw.pg.olat.data.entity.AbstractEntity;
import ch.bbw.pg.olat.data.repository.AbstractRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public abstract class AbstractService<T extends AbstractEntity, R extends AbstractRepository<T>> {

    protected R repository_;

    public AbstractService(@Autowired R repository) {
        repository_ = repository;
    }

    public T save(T entity) {
        return repository_.save(entity);
    }

    public Optional<T> get(Long id) {
        return repository_.findById(id);
    }

    public void delete(Long id) {
        T entity;
        if ((entity = get(id).orElse(null)) == null)
            return;
        repository_.delete(entity);
    }

    public List<T> findAll() {
        return repository_.findAll();
    }
}
