package com.javaica.avp.repository;

import com.javaica.avp.entity.TaskEntity;
import com.javaica.avp.model.GradedTaskHeader;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByStageIdOrderByIndex(long stageId);
    @Query("SELECT t.id, t.stage_id, t.name, t.description, t.index, ts.points FROM task t " +
            "JOIN stage s ON s.id = t.stage_id " +
            "JOIN course c ON s.course_id = c.id " +
            "JOIN course_group g ON g.course_id = c.id " +
            "JOIN team ON team.group_id = g.id " +
            "LEFT JOIN task_submission ts ON ts.team_id = team.id " +
            "WHERE s.id = :stageId AND team.id = :teamId " +
            "GROUP BY t.id " +
            "ORDER BY t.index")
    List<GradedTaskHeader> findAllByStageIdWithCalculatedPoints(long stageId, long teamId);

    @Query("SELECT ts.points FROM task t " +
            "JOIN stage s ON s.id = t.stage_id " +
            "JOIN course c ON s.course_id = c.id " +
            "JOIN course_group g ON g.course_id = c.id " +
            "JOIN team ON team.group_id = g.id " +
            "JOIN task_submission ts ON ts.team_id = team.id " +
            "WHERE t.id = :taskId " +
            "GROUP BY ts.id")
    Integer findTaskPointsById(long taskId);

    @Query("SELECT exists(" +
            "SELECT t.id FROM task t " +
            "JOIN stage s ON s.id = t.stage_id " +
            "JOIN course c ON s.course_id = c.id " +
            "JOIN course_group g ON g.course_id = c.id " +
            "JOIN team ON team.group_id = g.id " +
            "WHERE t.id = :taskId AND team.id = :teamId)")
    boolean existsByTeamId(long taskId, long teamId);
}
