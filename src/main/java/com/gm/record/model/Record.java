package com.gm.record.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gm.record.cachecordination.RecordServiceL2CacheCoordination;
import com.gm.record.servicelocator.RecordServiceBeanLocator;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.*;
import java.time.LocalDate;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static javax.persistence.GenerationType.AUTO;

/**
 * Domain model representing a Record
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Builder(toBuilder = false, builderMethodName = "newRecord")
@Getter
@Setter
@Cache
@Slf4j
public class Record {

    @Id
    @GeneratedValue(strategy = AUTO)
    @JsonIgnore
    public Long id;
    @Basic
    @Index
    public String name;
    @Basic
    public String address;
    @Basic
    public String postcode;
    @Basic
    @Index
    public String phone;
    @Basic
    public Double creditLimit;
    @Basic
    @JsonFormat(pattern = "d/MM/yyyy")
    public LocalDate birthday;
    @JsonIgnore
    @Basic
    @Version
    public Long ovn;

    @PostRemove
    @PostUpdate
    public void postChanges() {
        // Relay changes to all nodes in the cluster fot eh change.
        // We need to only relay in case of remove & update
        // NOTE: during POST phase id is always present
        RecordServiceL2CacheCoordination cacheCoordination = RecordServiceBeanLocator.beanByType(RecordServiceL2CacheCoordination.class);
        if (cacheCoordination != null) {
            log.info(format("Relay change for %s",this.toString()));
            RecordServiceBeanLocator.beanByType(RecordServiceL2CacheCoordination.class).invalidate(id);
        }
    }
}
