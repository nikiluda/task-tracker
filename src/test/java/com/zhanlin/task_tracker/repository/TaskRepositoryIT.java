package com.zhanlin.task_tracker.repository;

import com.zhanlin.task_tracker.entity.Task;
import com.zhanlin.task_tracker.entity.TaskStatus;
import com.zhanlin.task_tracker.identity.domain.User;
import com.zhanlin.task_tracker.identity.infrastructure.persistence.UserRepository;
import com.zhanlin.task_tracker.specification.TaskSpecification;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("int")
class TaskRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl
        );

        registry.add(
                "spring.datasource.username",
                postgres::getUsername
        );

        registry.add(
                "spring.datasource.password",
                postgres::getPassword
        );
    }

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findAll_shouldReturnOwnerTasks() {

        User owner = new User();
        owner.setEmail("owner@example.com");
        owner.setPassword("password");

        User anotherUser = new User();
        anotherUser.setEmail("another@example.com");
        anotherUser.setPassword("password");

        userRepository.save(owner);
        userRepository.save(anotherUser);

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.WAITING);
        task1.setOwner(owner);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setStatus(TaskStatus.DONE);
        task2.setOwner(owner);

        Task anotherTask = new Task();
        anotherTask.setTitle("Another task");
        anotherTask.setDescription("Another description");
        anotherTask.setStatus(TaskStatus.WAITING);
        anotherTask.setOwner(anotherUser);

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(anotherTask);

        taskRepository.flush();
        entityManager.clear();

        Page<Task> tasks = taskRepository.findAll(
                TaskSpecification.belongsToOwner(owner.getId()),
                PageRequest.of(0, 10)
        );

        assertThat(tasks.getContent())
                .hasSize(2)
                .allMatch(task ->
                        task.getOwner().getId().equals(owner.getId()));

        assertThat(tasks.getTotalElements())
                .isEqualTo(2);
    }

    @Test
    void findAll_shouldFilterByOwnerAndStatus() {

        User owner = new User();
        owner.setEmail("owner@example.com");
        owner.setPassword("password");

        userRepository.save(owner);

        Task waitingTask = new Task();
        waitingTask.setTitle("Waiting task");
        waitingTask.setDescription("Waiting description");
        waitingTask.setStatus(TaskStatus.WAITING);
        waitingTask.setOwner(owner);

        Task doneTask = new Task();
        doneTask.setTitle("Done task");
        doneTask.setDescription("Done description");
        doneTask.setStatus(TaskStatus.DONE);
        doneTask.setOwner(owner);

        taskRepository.save(waitingTask);
        taskRepository.save(doneTask);

        taskRepository.flush();
        entityManager.clear();

        Page<Task> tasks = taskRepository.findAll(
                TaskSpecification.belongsToOwner(owner.getId())
                        .and(TaskSpecification.hasStatus(TaskStatus.WAITING)),
                PageRequest.of(0, 10)
        );

        assertThat(tasks.getContent())
                .hasSize(1);

        assertThat(tasks.getContent().getFirst().getStatus())
                .isEqualTo(TaskStatus.WAITING);

        assertThat(tasks.getTotalElements())
                .isEqualTo(1);
    }

    @Test
    void findByIdAndOwnerId_shouldReturnTask_whenTaskBelongsToOwner() {

        User owner = new User();
        owner.setEmail("owner@example.com");
        owner.setPassword("password");

        userRepository.save(owner);

        Task task = new Task();
        task.setTitle("Test task");
        task.setDescription("Test description");
        task.setStatus(TaskStatus.WAITING);
        task.setOwner(owner);

        taskRepository.saveAndFlush(task);
        entityManager.clear();

        var foundTask = taskRepository
                .findByIdAndOwnerId(task.getId(), owner.getId());

        assertThat(foundTask)
                .isPresent();

        assertThat(foundTask.get().getId())
                .isEqualTo(task.getId());

        assertThat(foundTask.get().getOwner().getId())
                .isEqualTo(owner.getId());
    }

    @Test
    void findByIdAndOwnerId_shouldReturnEmpty_whenTaskBelongsToAnotherOwner() {

        User owner = new User();
        owner.setEmail("owner@example.com");
        owner.setPassword("password");

        User anotherOwner = new User();
        anotherOwner.setEmail("another@example.com");
        anotherOwner.setPassword("password");

        userRepository.save(owner);
        userRepository.save(anotherOwner);

        Task task = new Task();
        task.setTitle("Test task");
        task.setDescription("Test description");
        task.setStatus(TaskStatus.WAITING);
        task.setOwner(owner);

        taskRepository.saveAndFlush(task);
        entityManager.clear();

        var foundTask = taskRepository
                .findByIdAndOwnerId(task.getId(), anotherOwner.getId());

        assertThat(foundTask)
                .isEmpty();
    }
}