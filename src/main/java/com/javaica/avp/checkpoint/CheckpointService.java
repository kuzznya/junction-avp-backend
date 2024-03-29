package com.javaica.avp.checkpoint;

import com.javaica.avp.checkpoint.entity.CheckpointBlockEntity;
import com.javaica.avp.checkpoint.entity.CheckpointEntity;
import com.javaica.avp.checkpoint.entity.GradedCheckpointProjection;
import com.javaica.avp.checkpoint.model.Checkpoint;
import com.javaica.avp.checkpoint.model.CheckpointBlock;
import com.javaica.avp.checkpoint.model.CheckpointRequest;
import com.javaica.avp.checkpoint.model.GradedCheckpoint;
import com.javaica.avp.exception.ForbiddenException;
import com.javaica.avp.exception.NotFoundException;
import com.javaica.avp.security.AccessService;
import com.javaica.avp.stage.StageRepository;
import com.javaica.avp.submission.checkpoint.CheckpointSubmissionStatus;
import com.javaica.avp.user.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class CheckpointService {

    private final CheckpointRepository checkpointRepository;
    private final CheckpointBlockRepository blockRepository;
    private final StageRepository stageRepository;
    private final AccessService accessService;

    @Transactional
    public Checkpoint createCheckpoint(CheckpointRequest checkpoint) {
        if (!stageRepository.existsById(checkpoint.getStageId()))
            throw new NotFoundException("Stage " + checkpoint.getStageId() + " does not exist");
//        if (checkpointRepository.existsByStageId(checkpoint.getStageId()))
//            throw new AlreadyExistsException("Checkpoint already exists for this task");
        checkpointRepository.findByStageId(checkpoint.getStageId()).ifPresent(checkpointRepository::delete);

        CheckpointEntity entity = checkpointRepository.save(mapCheckpointModelToEntity(checkpoint));
        List<CheckpointBlockEntity> blocks = IntStream.range(0, checkpoint.getBlocks().size())
                .mapToObj(idx -> checkpoint.getBlocks().get(idx).withIndex(idx))
                .map(block -> blockModelToEntity(block, entity.getId()))
                .collect(Collectors.toList());
        blockRepository.saveAll(blocks);

         return mapCheckpointEntityToModel(entity);
    }

    public GradedCheckpoint getCheckpointById(Long checkpointId, AppUser user) {
        GradedCheckpoint checkpoint = checkpointRepository
                .findById(checkpointId)
                .map(checkpointEntity -> mapCheckpointEntityToGradedModel(checkpointEntity, user))
                .orElseThrow(() -> new NotFoundException("Checkpoint " + checkpointId + " does not exist"));
        if (!accessService.userHasAccessToStage(checkpoint.getStageId(), user))
            throw new ForbiddenException("User " + user.getUsername() + " does not have access to this course");
        return checkpoint;
    }


    public GradedCheckpoint getGradedCheckpointByStageId(Long stageId, long teamId) {
        return checkpointRepository.findByStageId(stageId)
                .map(checkpoint -> mapCheckpointEntityToGradedModel(checkpoint, teamId))
                .orElse(null);
    }

    private GradedCheckpoint mapCheckpointEntityToGradedModel(CheckpointEntity entity, long teamId) {
        GradedCheckpointProjection checkpointProjection = checkpointRepository
                .findByStageIdWithSubmission(entity.getStageId(), teamId)
                .orElse(null);
        return GradedCheckpoint.builder()
                .id(entity.getId())
                .stageId(entity.getStageId())
                .name(entity.getName())
                .description(entity.getDescription())
                .blocks(getBlocksForCheckpoint(entity.getId()))
                .status(Optional.ofNullable(checkpointProjection)
                        .map(GradedCheckpointProjection::getStatus)
                        .orElse(CheckpointSubmissionStatus.NEW))
                .points(Optional.ofNullable(checkpointProjection)
                        .map(GradedCheckpointProjection::getPoints)
                        .orElse(null))
                .build();
    }


    private GradedCheckpoint mapCheckpointEntityToGradedModel(CheckpointEntity entity, AppUser user) {
        return mapCheckpointEntityToGradedModel(entity, user.getTeamId());
    }

    private CheckpointEntity mapCheckpointModelToEntity(CheckpointRequest checkpoint) {
        return CheckpointEntity.builder()
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
