package com.javaica.avp.model;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.relational.core.mapping.Table;

public enum TaskSubmissionStatus {
    IN_REVIEW,
    ACCEPTED,
    DECLINED
}
