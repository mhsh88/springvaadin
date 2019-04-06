package ir.sharifi.spring.repository.test;

import ir.sharifi.spring.model.model.test.LeaveTimeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LeaveTimeTransactionRepository extends JpaRepository<LeaveTimeTransaction, UUID> {
}
