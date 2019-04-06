package ir.sharifi.spring.service.test.impl;

import ir.sharifi.spring.model.model.test.LeaveTimeTransaction;
import ir.sharifi.spring.repository.test.LeaveTimeTransactionRepository;
import ir.sharifi.spring.service.test.LeaveTimeTransactionService;
import ir.sharifi.spring.service.test.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LeaveTimeTransactionServiceImpl extends BaseServiceImpl<LeaveTimeTransaction, UUID> implements LeaveTimeTransactionService {

    @Autowired
    private LeaveTimeTransactionRepository leaveTimeTransactionRepository;

    @Override
    public LeaveTimeTransactionRepository getRepository() {
        return leaveTimeTransactionRepository;
    }

}
