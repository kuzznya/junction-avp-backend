package com.javaica.avp.submission.checkpoint;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckpointSubmissionAnswerRepository extends CrudRepository<CheckpointSubmissionAnswerEntity, Long> {
    List<CheckpointSubmissionAnswerEntity> findAllByCheckpointSubmissionId(long id);
}
