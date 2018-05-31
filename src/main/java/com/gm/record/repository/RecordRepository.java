package com.gm.record.repository;

import com.gm.record.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * A repository used for persisting & querying
 */
public interface RecordRepository extends JpaRepository<Record, Long>, JpaSpecificationExecutor<Record> {


    List<Record> findByName(String name);

    List<Record> findByPhone(String phonenumber);
}
