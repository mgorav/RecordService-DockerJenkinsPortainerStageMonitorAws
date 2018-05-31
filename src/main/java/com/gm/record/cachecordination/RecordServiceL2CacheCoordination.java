package com.gm.record.cachecordination;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.durableexecutor.DurableExecutorService;
import com.hazelcast.durableexecutor.DurableExecutorServiceFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

import static com.gm.record.hazelcast.RecordServiceHazelcastConfigBuilder.RECORD_SERVICE_CACHE_COORDINATION;

/**
 * A service responsible to relaying cache changes using {@link RecordL2CacheCoordinationCommand}
 */
@Component
@Slf4j
public class RecordServiceL2CacheCoordination {

    @Autowired
    private HazelcastInstance hazelcastInstance;


    public void invalidate(Long pk) {

        DurableExecutorService executorService = hazelcastInstance.getDurableExecutorService(RECORD_SERVICE_CACHE_COORDINATION);

        DurableExecutorServiceFuture<?> durableExecutor = executorService.submit(new RecordL2CacheCoordinationCommand(pk));
        try {
            durableExecutor.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }
    }
}
