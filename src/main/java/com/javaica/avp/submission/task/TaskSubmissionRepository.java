package com.javaica.avp.submission.task;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskSubmissionRepository extends CrudRepository<TaskSubmissionEntity, Long> {
    boolean existsByTaskIdAndTeamId(long taskId, long teamId);
    Optional<TaskSubmissionEntity> findByTaskId(long taskId);
}
