package com.javaica.avp.repository;

import com.javaica.avp.entity.TeamEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends CrudRepository<TeamEntity, Long> {
    List<TeamEntity> findAllByGroupId(long groupId);
    @Query("WITH gid (id) AS (SELECT t1.group_id AS id FROM team t1 WHERE t1.id = :teamId) " +
            "SELECT t.id, sum(ts.points) FROM team t " +
            "JOIN task_submission ts ON ts.team_id = t.id " +
            "WHERE t.group_id = (SELECT gid.id FROM gid) " +
            "GROUP BY t.id " +
            "ORDER BY sum(ts.points)")
    List<TeamEntity> getLeaderboard(long teamId);
}
