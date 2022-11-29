package com.javaica.avp.repository;

import com.javaica.avp.stage.StageEntity;
import com.javaica.avp.stage.StageHeader;
import com.javaica.avp.stage.StageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class StageTest {

    @Autowired
    private StageRepository repository;

    @Test
    public void findHeadersByCourseIdTest() {
        List<StageHeader> stageHeaderList = repository.findAllHeadersByCourseId(1, 1);

        assertEquals(stageHeaderList.size(), 5);
    }

    @Test
    public void findAllByCourseIdOrdered() {
        List<StageEntity> stageEntityList = repository.findAllByCourseIdOrderByIndex(1);

        assertEquals(stageEntityList.size(), 5);
    }
}
