package com.javaica.avp.submission.task;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("task_submission_answer")
@Value
@AllArgsConstructor
@Builder
public class TaskSubmissionAnswerEntity {
    @Id
    Long id;
    @With
    Long taskSubmissionId;
    Long taskBlockId;
    JsonNode content;
    boolean valid;
}
