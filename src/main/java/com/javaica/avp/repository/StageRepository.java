package com.javaica.avp.repository;


import com.javaica.avp.entity.StageEntity;
import com.javaica.avp.model.StageHeader;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StageRepository extends CrudRepository<StageEntity, Long> {
    @Query("SELECT id, name, description FROM stage WHERE course_id = :courseId")
    List<StageHeader> findAllHeadersByCourseId(long courseId);
}
