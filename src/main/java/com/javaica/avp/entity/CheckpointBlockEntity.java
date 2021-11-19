package com.javaica.avp.entity;

import com.javaica.avp.model.TaskBlockType;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("checkpoint_block")
@Value
public class CheckpointBlockEntity {
    @Id
    Long id;
    Long checkpointId;
    String content;
    TaskBlockType type;
}
