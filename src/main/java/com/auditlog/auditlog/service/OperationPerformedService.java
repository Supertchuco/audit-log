package com.auditlog.auditlog.service;

import com.auditlog.auditlog.dto.OperationPerformedDto;
import com.auditlog.auditlog.entity.OperationPerformed;
import com.auditlog.auditlog.exception.InvalidDateOperationPerformedException;
import com.auditlog.auditlog.exception.OperationPerformedAlreadyExistOnDatabaseException;
import com.auditlog.auditlog.exception.OperationPerformedNotFoundException;
import com.auditlog.auditlog.repository.OperationPerformedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Service
@Slf4j
@SuppressWarnings({"PMD.CommentDefaultAccessModifier", "PMD.PreserveStackTrace"})
public class OperationPerformedService {

    @Autowired
    OperationPerformedRepository operationPerformedRepository;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    public List<OperationPerformed> retrieveAllOperationsPerformed() {
        log.info("Retrieve all operations performed in database service");
        return operationPerformedRepository.findAll();
    }

    public OperationPerformed retrieveOperationPerformedById(final int operationId) {
        log.info("Retrieve operation performed by id service");
        final Optional<OperationPerformed> operationPerf = Optional.ofNullable(operationPerformedRepository.findById(operationId));
        return operationPerf.orElseThrow(OperationPerformedNotFoundException::new);
    }

    public OperationPerformed createOperationPerformed(final OperationPerformedDto operationPerformedDto) {
        log.info("Create operation performed service");
        validateOperationDateFormat(operationPerformedDto.getDate());
        validateIfOperationPerformedRegistryAlreadyExist(operationPerformedDto);
        return operationPerformedRepository.save(new OperationPerformed(
                sequenceGeneratorService.generateSequence(OperationPerformed.SEQUENCE_NAME), operationPerformedDto.getCustomerLogin(),
                operationPerformedDto.getCustomerName(), operationPerformedDto.getDate(), operationPerformedDto.getOperationCode(),
                operationPerformedDto.getOperationDescription()));
    }

    public boolean deleteOperationPerformed(final int operationId) {
        log.info("Delete operation performed service");
        operationPerformedRepository.delete(retrieveOperationPerformedById(operationId));
        return true;
    }

    private void validateOperationDateFormat(final String date) {
        log.info("Validate date [{}] if follow pattern: dd/MM/yyyy HH:mm:ss", date);
        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
            dateFormat.parse(date);
        } catch (Exception e) {
            log.error("Invalid date format {}", date);
            throw new InvalidDateOperationPerformedException();
        }
    }

    private void validateIfOperationPerformedRegistryAlreadyExist(final OperationPerformedDto operationPerformedDto) {
        final OperationPerformed operationPerformedDatabase = operationPerformedRepository
                .findByCustomerLoginAndCustomerNameAndDateAndOperationCode(operationPerformedDto.getCustomerLogin(),
                        operationPerformedDto.getCustomerName(), operationPerformedDto.getDate(),
                        operationPerformedDto.getOperationCode());
        if (nonNull(operationPerformedDatabase)) {
            log.error("This operation performed already exist on database");
            throw new OperationPerformedAlreadyExistOnDatabaseException();
        }
    }
}
