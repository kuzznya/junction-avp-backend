package com.javaica.avp.repository;

import com.javaica.avp.entity.CheckpointSubmissionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckpointSubmissionRepository extends CrudRepository<CheckpointSubmissionEntity, Long> {
    Optional<CheckpointSubmissionEntity> findByCheckpointId(long id);
}
