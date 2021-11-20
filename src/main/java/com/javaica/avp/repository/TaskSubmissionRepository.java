package com.javaica.avp.repository;

import com.javaica.avp.entity.TaskSubmissionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskSubmissionRepository extends CrudRepository<TaskSubmissionEntity, Long> {
    boolean existsByTaskIdAndTeamId(long taskId, long teamId);
}
