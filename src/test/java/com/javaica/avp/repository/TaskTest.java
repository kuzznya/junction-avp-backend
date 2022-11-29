package com.javaica.avp.repository;

import com.javaica.avp.task.TaskRepository;
import com.javaica.avp.task.entity.TaskEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class TaskTest {

    @Autowired
    private TaskRepository repository;

    @Test
    public void findAllByStageIdOrderByIndexTest() {
        List<TaskEntity> taskEntityList = repository.findAllByStageIdOrderByIndex(1);

        assertTrue(taskEntityList.get(0).getName().startsWith("Team building"));
    }
}
