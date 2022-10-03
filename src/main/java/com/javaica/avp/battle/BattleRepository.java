package com.javaica.avp.battle;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BattleRepository extends CrudRepository<BattleEntity, Long> {
    Optional<BattleEntity> findByInitiatorIdOrDefenderId(long initiatorId, long defenderId);
}
