package com.javaica.avp.submission.task;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskSubmissionAnswerRepository extends CrudRepository<TaskSubmissionAnswerEntity, Long> {
    List<TaskSubmissionAnswerEntity> findAllByTaskSubmissionId(long id);
}
