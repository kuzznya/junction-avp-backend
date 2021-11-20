package com.javaica.avp.repository;

import com.javaica.avp.entity.CheckpointSubmissionAnswerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckpointSubmissionAnswerRepository extends CrudRepository<CheckpointSubmissionAnswerEntity, Long> {
    void deleteAllByCheckpointSubmissionId(long id);
}
