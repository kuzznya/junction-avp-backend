package com.javaica.avp.repository;

import com.javaica.avp.entity.CheckpointBlockEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckpointBlockRepository extends CrudRepository<CheckpointBlockEntity, Long> {
    List<CheckpointBlockEntity> findAllByCheckpointIdOrderByIndex(long checkpointId);
}
