package com.javaica.avp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("course")
@Value
@AllArgsConstructor
@Builder
public class CourseEntity {
    @Id
    Long id;
    String name;
    String description;
}
