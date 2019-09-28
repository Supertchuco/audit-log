package com.auditlog.auditlog.controller;

import com.auditlog.auditlog.dto.OperationPerformedDto;
import com.auditlog.auditlog.entity.OperationPerformed;
import com.auditlog.auditlog.service.OperationPerformedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/operation", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Operation Performed controller")
@Slf4j
@Controller
@SuppressWarnings("PMD.CommentDefaultAccessModifier")
public class OperationPerformedLogTrackController {

    private static final String BAD_REQUEST = "Bad Request";
    private static final String INTERNAL_SERVER_ERROR = "Internal Server ErrorMessage";

    @Autowired
    OperationPerformedService operationPerformedService;

    /**
     * Create operation performed on database.
     *
     * @return 200 success
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Create operation performed on database.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = OperationPerformedDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = String.class),
            @ApiResponse(code = 500, message = "Internal Server ErrorMessage", response = String.class)
    })
    public OperationPerformed createOperationPerformed(@Valid @RequestBody OperationPerformedDto operationPerformedDto) {
        return operationPerformedService.createOperationPerformed(operationPerformedDto);
    }

    /**
     * Delete operation performed on database by id.
     *
     * @param operationId request data input
     * @return 200 success
     */
    @RequestMapping(value = "/{operationId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete operation performed on database by id.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = OperationPerformedDto.class),
            @ApiResponse(code = 400, message = "Bad Request", response = String.class),
            @ApiResponse(code = 500, message = "Internal Server ErrorMessage", response = String.class)
    })
    public boolean deleteOperationPerformedById(@PathVariable int operationId) {
        log.info("Delete operation performed on database by id :{}", operationId);
        return operationPerformedService.deleteOperationPerformed(operationId);
    }

    /**
     * Retrieve operation performed on database by id.
     *
     * @param operationId request data input
     * @return 200 success
     */
    @RequestMapping(value = "/{operationId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retrieve operation performed on database by id.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = OperationPerformed.class),
            @ApiResponse(code = 400, message = BAD_REQUEST, response = String.class),
            @ApiResponse(code = 500, message = INTERNAL_SERVER_ERROR, response = String.class)
    })
    public OperationPerformed retrieveOperationPerformedById(@PathVariable int operationId) {
        log.info("Retrieve operation performed on database by id :{}", operationId);
        return operationPerformedService.retrieveOperationPerformedById(operationId);
    }

    /**
     * Retrieve all operations performed on database.
     *
     * @return 200 success
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retrieve all operations performed on database.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = List.class),
            @ApiResponse(code = 400, message = BAD_REQUEST, response = String.class),
            @ApiResponse(code = 500, message = INTERNAL_SERVER_ERROR, response = String.class)
    })
    public List<OperationPerformed> retrieveAllOperationsPerformed() {
        log.info("Retrieve all operations performed on database");
        return operationPerformedService.retrieveAllOperationsPerformed();
    }
}
