package com.javaica.avp.entity;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("stage")
@Value
@AllArgsConstructor
public class StageEntity {
    @Id
    Long id;
    String name;
    String description;
}
