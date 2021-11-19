package com.javaica.avp.repository;

import com.javaica.avp.entity.GroupEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends CrudRepository<GroupEntity, Long> {
    List<GroupEntity> findAllByCourseId(long courseId);
}
