package com.synctech.task_management_system.repository;

import com.synctech.task_management_system.entity.Task;
import com.synctech.task_management_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByAssignedTo(User assignedTo);
}
