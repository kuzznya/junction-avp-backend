package com.javaica.avp.repository;

import com.javaica.avp.entity.TaskEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByStageId(Long stageId);

    @Query("SELECT exists(" +
            "SELECT t.id FROM task t " +
            "JOIN stage s ON s.id = t.id " +
            "JOIN course c ON s.course_id = c.id " +
            "JOIN course_group g ON g.course_id = c.id " +
            "JOIN team ON team.group_id = g.id " +
            "WHERE t.id = :taskId AND team.id = :teamId)")
    boolean existsByTeamId(long taskId, long teamId);
}
