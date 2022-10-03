package com.javaica.avp.task;

import com.javaica.avp.common.ContentBlockType;
import com.javaica.avp.task.entity.TaskBlockEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskBlockRepository extends CrudRepository<TaskBlockEntity, Long> {
    List<TaskBlockEntity> findAllByTaskIdOrderByIndex(long taskId);
    List<TaskBlockEntity> findAllByTaskIdAndType(long taskId, ContentBlockType type);
}
