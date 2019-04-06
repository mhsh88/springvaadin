package ir.sharifi.spring.model.model;

import java.io.Serializable;

public interface BaseEnum<E> extends Serializable {
    E getValue();
}
