package com.javaica.avp.repository;

import com.javaica.avp.entity.TaskBlockEntity;
import com.javaica.avp.model.TaskBlockType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskBlockRepository extends CrudRepository<TaskBlockEntity, Long> {
    List<TaskBlockEntity> findAllByTaskIdOrderByIndex(long taskId);
    List<TaskBlockEntity> findAllByTaskIdAndType(long taskId, TaskBlockType type);
}
