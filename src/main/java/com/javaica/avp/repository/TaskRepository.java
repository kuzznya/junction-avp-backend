package com.javaica.avp.repository;

import com.javaica.avp.entity.TaskEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByStageId(Long stageId);
}
