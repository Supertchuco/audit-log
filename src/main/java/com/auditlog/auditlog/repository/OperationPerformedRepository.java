package com.auditlog.auditlog.repository;

import com.auditlog.auditlog.entity.OperationPerformed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationPerformedRepository extends MongoRepository<OperationPerformed, Long> {

    OperationPerformed findById(int operationId);

    List<OperationPerformed> findAll();

    OperationPerformed findByCustomerLoginAndCustomerNameAndDateAndOperationCode(String customerLogin,
                                                                                 String customerName, String date, String operationCode);

}