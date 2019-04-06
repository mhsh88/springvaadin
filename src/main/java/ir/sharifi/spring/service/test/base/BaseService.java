package ir.sharifi.spring.service.test.base;

import ir.sharifi.spring.model.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T extends BaseEntity<I>, I extends Serializable> {
    JpaRepository<T, I> getRepository();
    T findById(I id);
    T getOne(I id);
    T insert(T model);
    @Transactional
    T update(T model);
    void delete(T model);
    List<T> getModels();
    Long getCount();
}