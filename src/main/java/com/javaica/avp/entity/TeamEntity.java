package com.javaica.avp.entity;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("team")
@Value
@AllArgsConstructor
public class TeamEntity {
    @Id
    Long id;
    String name;
    Long groupId;

}
