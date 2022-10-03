package com.javaica.avp.collab;


import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CollabRepository extends CrudRepository<CollabEntity, Long> {
    List<CollabEntity> findAllByRequesterIdOrHelperId(long requesterId, long helperId);
}
