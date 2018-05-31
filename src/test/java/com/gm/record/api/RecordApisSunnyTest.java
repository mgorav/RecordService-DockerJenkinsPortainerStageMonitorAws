package com.gm.record.api;

import com.gm.record.model.Record;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.PagedResources;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.LinkedHashMap;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
@TestPropertySource(properties = {"spring.cloud.consul.enabled=false",
        "spring.cloud.consul.discovery=false",
        "spring.cloud.bootstrap.enabled=false",
        "spring.cloud.consul.config.enabled=false"})
public class RecordApisSunnyTest {
    @Value("${local.server.port}")
    private int serverPort; //random port chosen by spring test
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testRecordApiGET() {

        PagedResources<Record> response = testRestTemplate.getForObject("/v1.0/api/records?search=name==*&page=0&size=7",
                PagedResources.class);

        PagedResources.PageMetadata pageMetadata = response.getMetadata();

        assertThat(pageMetadata.getTotalPages(), is(1L));
        assertThat(pageMetadata.getTotalElements(), is(7L));
        assertThat(pageMetadata.getSize(), is(7L));
        assertThat(response.getNextLink(), is(nullValue()));
        assertThat(response.getPreviousLink(), is(nullValue()));

        Iterator<?> iterator = response.getContent().iterator();

        while (iterator.hasNext()) {
            LinkedHashMap<String, String> recordData = (LinkedHashMap<String, String>) iterator.next();
            Record record = testRestTemplate.getForObject("/v1.0/api/records/name/" + recordData.get("name"), Record.class);
            assertThat(record, is(notNullValue()));

            record = testRestTemplate.getForObject("/v1.0/api/records/phone/" + recordData.get("phone"), Record.class);
            assertThat(record, is(notNullValue()));
        }
    }

}
