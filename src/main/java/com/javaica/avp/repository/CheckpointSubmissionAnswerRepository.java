package com.javaica.avp.repository;

import com.javaica.avp.entity.CheckpointSubmissionAnswerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckpointSubmissionAnswerRepository extends CrudRepository<CheckpointSubmissionAnswerEntity, Long> {
    void deleteAllByCheckpointSubmissionId(long id);
    List<CheckpointSubmissionAnswerEntity> findAllByCheckpointSubmissionId(long id);
}
