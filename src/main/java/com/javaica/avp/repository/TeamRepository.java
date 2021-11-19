package com.javaica.avp.repository;

import com.javaica.avp.entity.TeamEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends CrudRepository<TeamEntity, Long> {
    List<TeamEntity> findAllByGroupId(long groupId);
}
