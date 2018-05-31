package com.gm.record.api;

import com.gm.record.exception.advice.RecordAdviceObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
@TestPropertySource(properties = {"spring.cloud.consul.enabled=false",
        "spring.cloud.consul.discovery=false",
        "spring.cloud.bootstrap.enabled=false",
        "spring.cloud.consul.config.enabled=false"})
public class RecordApisNegativeTest {
    @Value("${local.server.port}")
    private int serverPort; //random port chosen by spring test
    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    public void testGetRecordsForNotFound() {

        RecordAdviceObject response = testRestTemplate.getForObject("/v1.0/api/records?search=name==blah&page=0&size=7",
                RecordAdviceObject.class);

        assertThat(response.getMessage(), containsString("Cannot find matching record for criteria:"));
    }

    @Test
    public void testGetRecordsForBadCriteria() {

        RecordAdviceObject response = testRestTemplate.getForObject("/v1.0/api/records?search=hi&page=0&size=7",
                RecordAdviceObject.class);

        assertThat(response.getMessage(), containsString(" Encountered \"<EOF>\" at line 1, column 2."));
    }

    @Test
    public void testPhoneNotFound() {

        RecordAdviceObject response = testRestTemplate.getForObject("/v1.0/api/records/phone/1",
                RecordAdviceObject.class);

        assertThat(response.getMessage(), containsString("Record with phone = 1 not found"));
    }

    @Test
    public void testNameNotFound() {

        RecordAdviceObject response = testRestTemplate.getForObject("/v1.0/api/records/name/blah",
                RecordAdviceObject.class);

        assertThat(response.getMessage(), containsString("Record with name = blah not found"));
    }


}
