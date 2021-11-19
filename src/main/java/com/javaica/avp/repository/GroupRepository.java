package com.javaica.avp.repository;

import com.javaica.avp.entity.GroupEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends CrudRepository<GroupEntity, Long> {
    List<GroupEntity> findAllByCourseId(long courseId);
    @Query("SELECT g.id, g.complexity_level, g.course_id FROM course_group g " +
            "JOIN team t ON g.id = t.group_id " +
            "WHERE t.id = :teamId")
    Optional<GroupEntity> findByTeamId(long teamId);
}
