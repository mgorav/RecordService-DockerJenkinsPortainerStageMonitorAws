package com.gm.record.cachecordination;

import com.gm.record.model.Record;
import com.gm.record.servicelocator.RecordServiceBeanLocator;
import lombok.AllArgsConstructor;

import javax.persistence.EntityManagerFactory;
import java.io.Serializable;

/**
 * A command which is relayed whenever there is changed in entity {@link Record}
 */
@AllArgsConstructor
public class RecordL2CacheCoordinationCommand implements Runnable, Serializable {

    final private Long pk;

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        EntityManagerFactory emf = RecordServiceBeanLocator.beanByType(EntityManagerFactory.class);
        emf.getCache().evict(Record.class, pk);
    }
}
