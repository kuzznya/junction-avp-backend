package com.javaica.avp.service;

import com.javaica.avp.entity.CheckpointEntity;
import com.javaica.avp.exception.AlreadyExistsException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.Checkpoint;
import com.javaica.avp.repository.CheckpointRepository;
import com.javaica.avp.repository.StageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckpointService {

    private final CheckpointRepository checkpointRepository;
    private final StageRepository stageRepository;

    public Checkpoint createCheckpoint(Checkpoint checkpoint) {
        if (!stageRepository.existsById(checkpoint.getStageId()))
            throw new NotFoundException("Stage " + checkpoint.getStageId() + " does not exist");
        if (checkpointRepository.existsByStageId(checkpoint.getStageId()))
            throw new AlreadyExistsException("Checkpoint already exists for this task");
        return mapCheckpointEntityToModel(
                checkpointRepository.save(mapCheckpointModelToEntity(checkpoint))
        );
    }

    private CheckpointEntity mapCheckpointModelToEntity(Checkpoint checkpoint) {
        return CheckpointEntity.builder()
                .stageId(checkpoint.getStageId())
                .name(checkpoint.getName())
                .description(checkpoint.getDescription())
                .build();
    }

    private Checkpoint mapCheckpointEntityToModel(CheckpointEntity entity) {
        return Checkpoint.builder()
                .stageId(entity.getStageId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public Checkpoint getCheckpointById(Long checkpointId) {
        return checkpointRepository
                .findById(checkpointId)
                .map(this::mapCheckpointEntityToModel)
                .orElseThrow(() -> new NotFoundException("Checkpoint " + checkpointId + " does not exist"));

    }
}
