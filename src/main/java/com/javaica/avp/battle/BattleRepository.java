package com.javaica.avp.battle;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BattleRepository extends CrudRepository<BattleEntity, Long> {
    List<BattleEntity> findAllByInitiatorIdOrDefenderId(long initiatorId, long defenderId);

    @Query("SELECT * FROM battle " +
            "WHERE (initiator_id = :teamId OR defender_id = :teamId) AND status IN ('PENDING', 'ACCEPTED')")
    Optional<BattleEntity> findActiveBattleByTeamId(long teamId);
}
