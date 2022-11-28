package com.javaica.avp.submission.checkpoint;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckpointSubmissionRepository extends CrudRepository<CheckpointSubmissionEntity, Long> {
    List<CheckpointSubmissionEntity> findByCheckpointId(long id);
    Optional<CheckpointSubmissionEntity> findByCheckpointIdAndTeamId(long checkpointId, long teamId);
    @Query("UPDATE checkpoint_submission SET points = points * 5 / 4 WHERE id = :submissionId")
    @Modifying
    void updateCheckpointSubmissionOnBattleWon(long submissionId);
}
