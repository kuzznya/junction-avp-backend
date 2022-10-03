package com.javaica.avp.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("team")
@Value
@AllArgsConstructor
@Builder
public class TeamEntity {
    @Id
    Long id;
    String name;
    Long groupId;
}
