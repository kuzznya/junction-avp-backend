package com.javaica.avp.checkpoint;

import com.javaica.avp.checkpoint.entity.CheckpointBlockEntity;
import com.javaica.avp.common.ContentBlockType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckpointBlockRepository extends CrudRepository<CheckpointBlockEntity, Long> {
    List<CheckpointBlockEntity> findAllByCheckpointIdOrderByIndex(long checkpointId);
    List<CheckpointBlockEntity> findAllByCheckpointIdAndType(long id, ContentBlockType type);
}
