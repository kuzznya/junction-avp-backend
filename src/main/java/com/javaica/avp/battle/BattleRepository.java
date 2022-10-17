package com.javaica.avp.battle;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BattleRepository extends CrudRepository<BattleEntity, Long> {
    List<BattleEntity> findAllByInitiatorIdOrDefenderId(long initiatorId, long defenderId);

    Optional<BattleEntity> findByInitiatorIdOrDefenderId(long initiatorId, long defenderId);

    List<BattleEntity> findByInitiatorId(long initiatorId);

    List<BattleEntity> findByDefenderId(long defenderId);
}
