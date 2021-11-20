package com.javaica.avp.entity;

import com.javaica.avp.model.CollabStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("collab")
@Value
@AllArgsConstructor
@Builder
public class CollabEntity {
    @Id
    Long id;
    Long requesterId;
    Long helperId;
    @With
    CollabStatus status;
}
