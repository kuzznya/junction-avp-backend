package com.javaica.avp.service;

import com.javaica.avp.entity.CheckpointBlockEntity;
import com.javaica.avp.entity.CheckpointEntity;
import com.javaica.avp.exception.AlreadyExistsException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.model.Checkpoint;
import com.javaica.avp.model.CheckpointBlock;
import com.javaica.avp.repository.CheckpointBlockRepository;
import com.javaica.avp.repository.CheckpointRepository;
import com.javaica.avp.repository.StageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class CheckpointService {

    private final CheckpointRepository checkpointRepository;
    private final CheckpointBlockRepository blockRepository;
    private final StageRepository stageRepository;

    @Transactional
    public Checkpoint createCheckpoint(Checkpoint checkpoint) {
        if (!stageRepository.existsById(checkpoint.getStageId()))
            throw new NotFoundException("Stage " + checkpoint.getStageId() + " does not exist");
        if (checkpointRepository.existsByStageId(checkpoint.getStageId()))
            throw new AlreadyExistsException("Checkpoint already exists for this task");

        CheckpointEntity entity = checkpointRepository.save(mapCheckpointModelToEntity(checkpoint.withId(null)));
        List<CheckpointBlockEntity> blocks = IntStream.range(0, checkpoint.getBlocks().size())
                .mapToObj(idx -> checkpoint.getBlocks().get(idx).withIndex(idx))
                .map(block -> blockModelToEntity(block, entity.getId()))
                .collect(Collectors.toList());
        blockRepository.saveAll(blocks);

         return mapCheckpointEntityToModel(entity);
    }

    // TODO add user check & add user's info (submission status like in StageService)
    public Checkpoint getCheckpointById(Long checkpointId) {
        return checkpointRepository
                .findById(checkpointId)
                .map(this::mapCheckpointEntityToModel)
                .orElseThrow(() -> new NotFoundException("Checkpoint " + checkpointId + " does not exist"));

    }

    private CheckpointEntity mapCheckpointModelToEntity(Checkpoint checkpoint) {
        return CheckpointEntity.builder()
                .id(checkpoint.getId())
                .stageId(checkpoint.getStageId())
                .name(checkpoint.getName())
                .description(checkpoint.getDescription())
                .build();
    }

    private Checkpoint mapCheckpointEntityToModel(CheckpointEntity entity) {
        return Checkpoint.builder()
                .id(entity.getId())
                .stageId(entity.getStageId())
                .name(entity.getName())
                .description(entity.getDescription())
                .blocks(getBlocksForCheckpoint(entity.getId()))
                .build();
    }

    private List<CheckpointBlock> getBlocksForCheckpoint(long id) {
        return blockRepository.findAllByCheckpointIdOrderByIndex(id)
                .stream()
                .map(this::blockEntityToModel)
                .collect(Collectors.toList());
    }

    private CheckpointBlockEntity blockModelToEntity(CheckpointBlock block, long checkpointId) {
        return CheckpointBlockEntity.builder()
                .id(block.getId())
                .checkpointId(checkpointId)
                .index(block.getIndex())
                .type(block.getType())
                .content(block.getContent())
                .build();
    }

    private CheckpointBlock blockEntityToModel(CheckpointBlockEntity entity) {
        return CheckpointBlock.builder()
                .id(entity.getId())
                .type(entity.getType())
                .content(entity.getContent())
                .index(entity.getIndex())
                .build();
    }
}
