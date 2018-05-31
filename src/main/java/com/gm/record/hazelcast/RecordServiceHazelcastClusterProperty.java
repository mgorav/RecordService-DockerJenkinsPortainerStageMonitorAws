package com.gm.record.hazelcast;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "record-service.cluster")
@Getter
@Setter
/**
 * Cluster properties
 */
public class RecordServiceHazelcastClusterProperty {
    String members;
}
