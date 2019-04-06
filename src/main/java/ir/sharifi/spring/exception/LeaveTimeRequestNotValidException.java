package ir.sharifi.spring.exception;

import javax.persistence.PersistenceException;

public class LeaveTimeRequestNotValidException extends PersistenceException {

    public LeaveTimeRequestNotValidException() {
        super();
    }

    public LeaveTimeRequestNotValidException(String message) {
        super(message);
    }
}
