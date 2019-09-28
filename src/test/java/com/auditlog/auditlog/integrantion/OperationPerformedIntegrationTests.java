package com.auditlog.auditlog.integrantion;

import com.auditlog.auditlog.dto.OperationPerformedDto;
import com.auditlog.auditlog.entity.OperationPerformed;
import com.auditlog.auditlog.enumerator.ErrorMessages;
import com.auditlog.auditlog.service.OperationPerformedService;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration for API test implementation.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SuppressWarnings({"PMD.TooManyMethods", "PMD.SignatureDeclareThrowsException", "checkstyle:AbbreviationAsWordInName"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OperationPerformedIntegrationTests {

    private static final String BASE_ENDPOINT = "http://localhost:8090/audit-log/operation/";

    private String payload;

    private HttpEntity<String> entity;

    private ResponseEntity<String> response;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OperationPerformedService operationPerformedService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private MongodExecutable mongodExecutable;

    @After
    public void clean() {
        mongodExecutable.stop();
    }

    @Before
    public void setup() throws Exception {
        final String hostIp = "localhost";
        final int port = 26920;

        final IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                .net(new Net(hostIp, port, Network.localhostIsIPv6()))
                .build();

        final MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
    }

    /**
     * Read json.
     *
     * @param filename file name input
     * @return String file content
     */
    private static String readJson(final String filename) {
        try {
            return FileUtils.readFileToString(ResourceUtils.getFile("classpath:" + filename), "UTF-8");
        } catch (IOException exception) {
            return null;
        }
    }

    /**
     * Build Http headers.
     *
     * @return Http Headers object
     */
    private HttpHeaders buildHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * Test scenario when insert new operation performed with success.
     */
    @Test
    public void shouldReturn200WhenInsertNewOperationPerformedRegistry() {
        payload = readJson("request/insertOperationPerformedSuccess.json");
        entity = new HttpEntity<String>(payload, buildHttpHeaders());
        response = testRestTemplate.exchange(BASE_ENDPOINT, HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test scenario when insert new operation performed with invalid date.
     */
    @Test
    public void shouldReturn400WhenInsertNewOperationPerformedWithInvalidDate() {
        payload = readJson("request/insertOperationPerformedInvalidDateFormat.json");
        entity = new HttpEntity<String>(payload, buildHttpHeaders());
        response = testRestTemplate.exchange(BASE_ENDPOINT, HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains(ErrorMessages.INVALID_DATE_FORMAT.getMessage()));
    }

    /**
     * Test scenario when insert duplicated operation performed.
     */
    @Test
    public void shouldReturn400WhenInsertNewOperationDuplicatedOnDatabase() throws IOException {
        insertData("request/insertOperationPerformedSuccess_3.json");
        payload = readJson("request/insertOperationPerformedSuccess_3.json");
        entity = new HttpEntity<String>(payload, buildHttpHeaders());
        response = testRestTemplate.exchange(BASE_ENDPOINT, HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains(ErrorMessages.OPERATION_PERFORMED_ALREADY_EXIST_ON_DATABASE.getMessage()));
    }

    /**
     * Test scenario when retrieve all operations performed on database.
     */
    @Test
    public void shouldReturn200WhenRetrieveAllOperationsPerformedOnDatabase() throws IOException {
        insertData("request/insertOperationPerformedSuccess_6.json");
        entity = new HttpEntity<String>(buildHttpHeaders());
        response = testRestTemplate.exchange(BASE_ENDPOINT, HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("saga"));
    }

    /**
     * Test scenario when retrieve operations performed by id.
     */
    @Test
    public void shouldReturn200WhenRetrieveOperationsPerformedById() throws IOException {
        final Long opId = insertData("request/insertOperationPerformedSuccess_4.json");
        entity = new HttpEntity<String>(buildHttpHeaders());
        response = testRestTemplate.exchange(BASE_ENDPOINT.concat(opId.toString()), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("mbison"));
    }

    /**
     * Test scenario when retrieve operations performed by id and operation not exist on database.
     */
    @Test
    public void shouldReturn400WhenRetrieveOperationsPerformedByIdAndOperationNotExistOnDatabase() {
        entity = new HttpEntity<String>(buildHttpHeaders());
        response = testRestTemplate.exchange(BASE_ENDPOINT.concat("69"), HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains(ErrorMessages.OPERATION_PERFORMED_NOT_FOUND.getMessage()));
    }

    /**
     * Test scenario when delete operation performed by id.
     */
    @Test
    public void shouldReturn200WhenDeleteOperationPerformedById() throws IOException {
        final Long opId = insertData("request/insertOperationPerformedSuccess_5.json");
        entity = new HttpEntity<String>(buildHttpHeaders());
        response = testRestTemplate.exchange(BASE_ENDPOINT.concat(opId.toString()), HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("true"));
    }

    /**
     * Test scenario when delete operation performed by id and operation not exist on database.
     */
    @Test
    public void shouldReturn200WhenDeleteOperationPerformedByIdAndOperationNotExistOnDatabase() throws IOException {
        entity = new HttpEntity<String>(buildHttpHeaders());
        response = testRestTemplate.exchange(BASE_ENDPOINT.concat("666"), HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains(ErrorMessages.OPERATION_PERFORMED_NOT_FOUND.getMessage()));
    }

    private Long insertData(final String jsonName) throws IOException {
        final OperationPerformed operationPerformed = operationPerformedService.createOperationPerformed(objectMapper.readValue(readJson(jsonName),
                OperationPerformedDto.class));
        return operationPerformed.getId();
    }

}
