package com.gm.record.service;

import com.gm.record.exception.RecordNotFoundException;
import com.gm.record.repository.RecordRepository;
import com.gm.record.model.Record;
import com.gm.record.rsql.RecordRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.queries.ReadObjectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.data.domain.PageRequest.of;

/**
 * A service which opens transaction and delegates to the {@link RecordRepository}
 */
@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;
    @PersistenceContext
    private EntityManager em;


    @Transactional
    public Page<Record> findByCriteria(String search, int page, int size) {

        Node rootNode = new RSQLParser().parse(search);
        Specification<Record> spec = rootNode.accept(new RecordRsqlVisitor<Record>());

        return recordRepository.findAll(spec, of(page, size));
    }


    @Transactional
    public void save(List<Record> records) {

        recordRepository.saveAll(records);
    }

    @Transactional(readOnly = true)
    public Record findByName(String name) {

        List<Record> records = recordRepository.findByName(name);

        return !records.isEmpty() ? records.get(0) : null;
    }

    @Transactional(readOnly = true)
    public Record findByPhone(String phone) {
        List<Record> records = recordRepository.findByPhone(phone);

        return !records.isEmpty() ? records.get(0) : null;
    }

    @Transactional
    public Record create(Record record) {
        return recordRepository.save(record);
    }

    @Transactional
    public Record update(Record record) {
        ReadAllQuery query = new ReadAllQuery();
        query.setExampleObject(record);
        EntityManagerImpl emIml = getNativeEM();
        List<Record> result = (List<Record>) emIml.getActiveSession().executeQuery(query);

        assertResults(result, format("Cannot found record %s for update", record), format("More than one records[%s] found for update", result.size()));

        return em.merge(result.get(0));

    }

    @Transactional
    public void delete(Record record) {

        ReadObjectQuery query = new ReadObjectQuery();
        query.setExampleObject(record);
        EntityManagerImpl emIml = getNativeEM();
        List<Record> result = (List<Record>) emIml.getActiveSession().executeQuery(query);

        assertResults(result, format("Cannot found record %s for delete", record), format("More than one records[%s] found for delete", result.size()));

        recordRepository.delete(result.get(0));

    }

    // Utility methods
    private void assertResults(List<Record> result, String noRecFoundMsg, String moreThanOneRecFoundMsg) {
        if (result.isEmpty()) {
            throw new RecordNotFoundException(noRecFoundMsg);
        } else if (result.size() > 1) {
            throw new RuntimeException(moreThanOneRecFoundMsg);
        }
    }

    private EntityManagerImpl getNativeEM() {
        Object isNativeEM = em.getDelegate();
        if (isNativeEM instanceof EntityManagerImpl) {
            return EntityManagerImpl.class.cast(isNativeEM);
        }
        return EntityManagerImpl.class.cast(isNativeEM);
    }
}
