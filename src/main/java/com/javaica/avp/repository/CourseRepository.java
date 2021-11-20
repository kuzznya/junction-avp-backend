package com.javaica.avp.repository;

import com.javaica.avp.entity.CourseEntity;
import com.javaica.avp.entity.TeamEntity;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends CrudRepository<CourseEntity, Long> {
    @Query("SELECT exists(" +
            "SELECT c.id FROM course c " +
            "JOIN course_group g ON g.course_id = c.id " +
            "JOIN team t ON t.group_id = g.id " +
            "WHERE t.id = :teamId AND c.id = :courseId)")
    boolean courseExistsWithTeamId(long courseId, long teamId);

    @Query("SELECT * FROM course c " +
            "JOIN course_group g on g.course_id = c.id " +
            "JOIN team t on t.group_id = g.id " +
            "WHERE t.id = :teamId"
    )
    Optional<CourseEntity> findCourseByTeamId(long teamId);
}
