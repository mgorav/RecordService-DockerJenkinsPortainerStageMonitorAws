package com.gm.record.rsql;

import com.gm.record.model.Record;
import com.gm.record.repository.RecordRepository;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
@TestPropertySource(properties = {"spring.cloud.consul.enabled=false",
        "spring.cloud.consul.discovery=false",
        "spring.cloud.bootstrap.enabled=false",
        "spring.cloud.consul.config.enabled=false"})
public class RecordRsqlVisitorTest {
    @Autowired
    private RecordRepository recordRepository;


    @Test
    public void testUsingSpecification() {


        Node rootNode = new RSQLParser().parse("name==*");
        Specification<Record> spec = rootNode.accept(new RecordRsqlVisitor<Record>());

        assertThat(recordRepository.findAll(spec).size(), is(7));
    }
}
