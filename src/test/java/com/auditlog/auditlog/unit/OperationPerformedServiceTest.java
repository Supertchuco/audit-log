package com.auditlog.auditlog.unit;

import com.auditlog.auditlog.dto.OperationPerformedDto;
import com.auditlog.auditlog.entity.OperationPerformed;
import com.auditlog.auditlog.exception.InvalidDateOperationPerformedException;
import com.auditlog.auditlog.exception.OperationPerformedAlreadyExistOnDatabaseException;
import com.auditlog.auditlog.repository.OperationPerformedRepository;
import com.auditlog.auditlog.service.OperationPerformedService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

/**
 * Operation performed service unit tests.
 */
public class OperationPerformedServiceTest {

    @InjectMocks
    private OperationPerformedService operationPerformedService;

    @Mock
    private OperationPerformedRepository operationPerformedRepository;

    private Object[] inputArray;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldNotThrowExceptionWhenValidateDate() {
        inputArray = new Object[]{"2019/09/26 05:04:13"};
        ReflectionTestUtils.invokeMethod(operationPerformedService, "validateOperationDateFormat", inputArray);
    }

    @Test(expected = InvalidDateOperationPerformedException.class)
    public void shouldThrowInvalidDateOperationPerformedExceptionWhenValidateInvalidDate() {
        inputArray = new Object[]{"2019/09/26"};
        ReflectionTestUtils.invokeMethod(operationPerformedService, "validateOperationDateFormat", inputArray);
    }

    @Test
    public void shouldNotThrowExceptionWhenValidateIfOperationAlreadyExistOnDatabase() {
        inputArray = new Object[]{buildOperationPerformedDto()};
        ReflectionTestUtils.invokeMethod(operationPerformedService, "validateIfOperationPerformedRegistryAlreadyExist", inputArray);
    }

    @Test(expected = OperationPerformedAlreadyExistOnDatabaseException.class)
    public void shouldThrowOperationPerformedAlreadyExistOnDatabaseExceptionWhenValidateIfOperationAlreadyExistOnDatabase() {
        doReturn(new OperationPerformed()).when(operationPerformedRepository)
                .findByCustomerLoginAndCustomerNameAndDateAndOperationCode(anyString(), anyString(), anyString(), anyString());
        inputArray = new Object[]{buildOperationPerformedDto()};
        ReflectionTestUtils.invokeMethod(operationPerformedService, "validateIfOperationPerformedRegistryAlreadyExist", inputArray);
    }

    private OperationPerformedDto buildOperationPerformedDto() {
        final OperationPerformedDto operation = new OperationPerformedDto();
        operation.setCustomerLogin("BallAndre");
        operation.setCustomerName("Andre");
        operation.setDate("2019/09/26 05:04:13");
        operation.setOperationCode("PAU");
        operation.setOperationDescription("Pay unit");
        return operation;
    }
}
