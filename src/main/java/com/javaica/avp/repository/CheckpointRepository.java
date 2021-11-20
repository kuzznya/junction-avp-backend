package com.javaica.avp.repository;

import com.javaica.avp.entity.CheckpointEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CheckpointRepository extends CrudRepository<CheckpointEntity, Long> {
    Optional<CheckpointEntity> findByStageId(Long stageId);
    boolean existsByStageId(long stageId);
}
