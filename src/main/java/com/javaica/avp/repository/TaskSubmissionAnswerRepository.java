package com.javaica.avp.repository;

import com.javaica.avp.entity.TaskSubmissionAnswerEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskSubmissionAnswerRepository extends CrudRepository<TaskSubmissionAnswerEntity, Long> {
    List<TaskSubmissionAnswerEntity> findAllByTaskSubmissionId(long id);
}
