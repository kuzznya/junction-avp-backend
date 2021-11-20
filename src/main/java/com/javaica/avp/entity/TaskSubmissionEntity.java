package com.javaica.avp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("task_submission")
@Value
@AllArgsConstructor
@Builder
public class TaskSubmissionEntity {
    @Id
    Long id;
    Long teamId;
    Long taskId;
    int points;
}
