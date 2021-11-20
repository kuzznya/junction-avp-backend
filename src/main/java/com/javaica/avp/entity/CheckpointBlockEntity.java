package com.javaica.avp.entity;

import com.javaica.avp.model.ContentBlockType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("checkpoint_block")
@Value
@AllArgsConstructor
@Builder
public class CheckpointBlockEntity {
    @Id
    Long id;
    Long checkpointId;
    String content;
    ContentBlockType type;
    Integer index;
}
