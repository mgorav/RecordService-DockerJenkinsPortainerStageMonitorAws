package com.gm.record.loader;

import com.gm.record.service.RecordService;
import com.gm.record.model.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xbib.jdbc.csv.CsvStatement;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.valueOf;
import static java.lang.String.format;
import static java.sql.DriverManager.getConnection;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * Loads the data from CSV file
 */
@Service
@Slf4j
public class RecordDataLoader {

    private Connection connection;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private RecordService recordService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void loadRecordsFromCsv() throws SQLException {
        if (connection != null) {
            try {
                CsvStatement statement = (CsvStatement) connection.createStatement();
                String query = "SELECT * FROM Workbook";

                statement.execute(query);
                ResultSet rs = statement.getResultSet();
                List<Record> records = new ArrayList<>();

                log.info("Loading data from Workbook.csv");
                while (rs.next()) {
                    records.add(Record.newRecord()
                            .name(rs.getString(1))
                            .address(rs.getString(2))
                            .postcode(rs.getString(3))
                            .phone(rs.getString(4))
                            .creditLimit(valueOf(rs.getString(5)))
                            .birthday(LocalDate.parse(rs.getString(6), ofPattern("d/MM/yyyy")))
                            .build());
                }

                recordService.save(records);

            } finally {
                connection.close();
                log.info("Loading data from Workbook.csv completed");
            }
        }

    }


    @PostConstruct
    public void postConstruct() {

        try {
            connection = getConnection("jdbc:xbib:csv:src/main/resources/data");
            System.setProperty("line.separator", "\n");
        } catch (SQLException e) {
            log.info(format("Workbook.csv not found"));
        }

    }
}
