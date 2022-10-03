package com.javaica.avp.team;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GradedTeamProjection {
    Long id;
    String name;
    Long groupId;
    int points;

    public GradedTeamProjection(Long id, String name, Long groupId, Integer points) {
        this.id = id;
        this.name = name;
        this.groupId = groupId;
        this.points = points != null ? points : 0;
    }
}
