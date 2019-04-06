package ir.sharifi.spring.exception;

import javax.persistence.PersistenceException;

public class LeaveTimeRequestAmountException extends PersistenceException {

    public LeaveTimeRequestAmountException() {
        super();
    }

    public LeaveTimeRequestAmountException(String message) {
        super(message);
    }
}
