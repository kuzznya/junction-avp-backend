package com.javaica.avp.repository;

import com.javaica.avp.entity.BattleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BattleRepository extends CrudRepository<BattleEntity, Long> {
    Optional<BattleEntity> findByInitiatorIdOrDefenderId(long initiatorId, long defenderId);
}
