package com.javaica.avp.repository;


import com.javaica.avp.entity.StageEntity;
import com.javaica.avp.model.StageHeader;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StageRepository extends CrudRepository<StageEntity, Long> {
    @Query("SELECT s.id, s.name, s.description, coalesce(cs.status, 'NEW') AS status FROM stage s " +
            "LEFT JOIN checkpoint c ON c.stage_id = s.id " +
            "LEFT JOIN checkpoint_submission cs ON cs.checkpoint_id = c.id " +
            "WHERE s.course_id = :courseId AND (cs.team_id IS NULL OR cs.team_id = :teamId)")
    List<StageHeader> findAllHeadersByCourseId(long courseId, long teamId);
    List<StageEntity> findAllByCourseId(long courseId);
}
