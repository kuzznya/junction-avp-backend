package com.javaica.avp.repository;

import com.javaica.avp.entity.TaskBlockEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskBlockRepository extends CrudRepository<TaskBlockEntity, Long> {
    List<TaskBlockEntity> findAllByTaskId(Long taskId);
}
