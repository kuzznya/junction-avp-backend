package com.javaica.avp.repository;

import com.javaica.avp.entity.BattleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BattleRepository extends CrudRepository<BattleEntity, Long> {
    List<BattleEntity> findAllByInitiatorIdOrDefenderId(long initiatorId, long defenderId);
}
