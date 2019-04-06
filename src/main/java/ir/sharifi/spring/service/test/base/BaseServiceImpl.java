package ir.sharifi.spring.service.test.base;

import ir.sharifi.spring.model.model.BaseEntity;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T extends BaseEntity<I>, I extends Serializable> implements BaseService<T, I> {

    @Override
    public T findById(I id) {
        Optional<T> optionalModel = getRepository().findById(id);
        if (!optionalModel.isPresent() || optionalModel.get().getDeleted()) {
            return null;
        }
        return optionalModel.get();
    }

    @Override
    public T getOne(I id) {
        T model = getRepository().getOne(id);
        if (model.getDeleted()) {
            throw new EntityNotFoundException();
        }
        return model;
    }

    @Override
    public T insert(T model) {
        return getRepository().save(model);
    }

    @Override
    public T update(T model) {
        return getRepository().save(model);
    }

    @Override
    public void delete(T model) {
        getRepository().delete(model);
    }

    @Override
    public List<T> getModels() {
        return getRepository().findAll();
    }

    @Override
    public Long getCount() {
        return getRepository().count();
    }
}
