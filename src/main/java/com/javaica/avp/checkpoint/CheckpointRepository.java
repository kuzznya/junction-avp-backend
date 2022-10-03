package com.javaica.avp.checkpoint;

import com.javaica.avp.checkpoint.entity.CheckpointEntity;
import com.javaica.avp.checkpoint.entity.GradedCheckpointProjection;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CheckpointRepository extends CrudRepository<CheckpointEntity, Long> {
    Optional<CheckpointEntity> findByStageId(long stageId);
    boolean existsByStageId(long stageId);
    @Query("SELECT cp.id, cp.stage_id, cp.name, cp.description, cs.status, cs.points FROM checkpoint cp " +
            "LEFT JOIN checkpoint_submission cs ON cs.checkpoint_id = cp.id " +
            "WHERE cp.stage_id = :stageId AND (cs.team_id IS NULL OR cs.team_id = :teamId)")
    Optional<GradedCheckpointProjection> findByStageIdWithSubmission(long stageId, long teamId);
}
