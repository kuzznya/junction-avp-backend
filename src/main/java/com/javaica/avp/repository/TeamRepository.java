package com.javaica.avp.repository;

import com.javaica.avp.entity.GradedTeamProjection;
import com.javaica.avp.entity.TeamEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<TeamEntity, Long> {
    List<TeamEntity> findAllByGroupId(long groupId);
    @Query("SELECT t.id, t.name, t.group_id, coalesce(sum(ts.points) + sum(cs.points), sum(ts.points), sum(cs.points), 0) AS points FROM team t " +
            "LEFT JOIN task_submission ts ON ts.team_id = t.id " +
            "LEFT JOIN checkpoint_submission cs ON cs.team_id = t.id " +
            "WHERE t.group_id = (SELECT t1.group_id FROM team t1 WHERE t1.id = :teamId) " +
            "GROUP BY t.id " +
            "ORDER BY coalesce(sum(ts.points) + sum(cs.points), sum(ts.points), sum(cs.points), 0) DESC")
    List<GradedTeamProjection> getLeaderboard(long teamId);
    @Query("SELECT t.id, t.name, t.group_id, coalesce(sum(ts.points) + sum(cs.points), sum(ts.points), sum(cs.points), 0) AS points FROM team t " +
            "LEFT JOIN task_submission ts ON ts.team_id = t.id " +
            "LEFT JOIN checkpoint_submission cs ON cs.team_id = t.id " +
            "WHERE t.id = :teamId " +
            "GROUP BY t.id")
    Optional<GradedTeamProjection> findByIdWithPoints(long teamId);
}
