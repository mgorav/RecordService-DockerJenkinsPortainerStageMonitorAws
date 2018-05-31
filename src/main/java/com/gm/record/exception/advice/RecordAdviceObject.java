package com.gm.record.exception.advice;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
/**
 * A depiction of an advice in case of an error
 */
public class RecordAdviceObject {

    private String message;
}
