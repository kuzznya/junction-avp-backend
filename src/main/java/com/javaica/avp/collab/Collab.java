package com.javaica.avp.collab;

import com.javaica.avp.team.TeamHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class Collab {
    Long id;
    TeamHeader requester;
    TeamHeader helper;
    CollabStatus status;
}
