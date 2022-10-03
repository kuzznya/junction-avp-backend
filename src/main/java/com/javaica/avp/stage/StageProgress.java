package com.javaica.avp.stage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class StageProgress {
    String name;
    boolean done;
}
