package com.javaica.avp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("course_group")
@Value
@AllArgsConstructor
@Builder
public class GroupEntity {
    @Id
    Long id;
    Integer complexityLevel;
    Long courseId;
}
