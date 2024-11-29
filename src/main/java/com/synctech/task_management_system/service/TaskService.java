package com.synctech.task_management_system.service;

import com.synctech.task_management_system.dto.CreateTaskRequest;
import com.synctech.task_management_system.entity.*;
import com.synctech.task_management_system.exception.CustomServiceException;
import com.synctech.task_management_system.repository.TaskRepository;
import com.synctech.task_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Task create(CreateTaskRequest taskRequest, User user) {
        var task = Task
                .builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .priority(Priority.valueOf(taskRequest.getPriority()))
                .status(Status.PENDING)
                .dueDate(taskRequest.getDueDate())
                .createdBy(user)
                .build();

        task = taskRepository.save(task);

        return task;
    }

    public List<Task> getAll(User user) {
        if (user.getRole() == Role.ROLE_MANAGER) {
            // If the user is a manager, fetch all tasks
            return taskRepository.findAll();
        } else if (user.getRole() == Role.ROLE_EMPLOYEE) {
            // If the user is an employee, fetch tasks assigned to them
            return taskRepository.findByAssignedTo(user);
        } else {
            throw new CustomServiceException("User role not recognized");
        }
    }

    public Task assignTask(String taskId, String userId) {
        if (taskId == null || !isValidUUID(taskId)) {
            throw new CustomServiceException("Invalid Task ID format");
        }

        if (userId == null || !isValidUUID(userId)) {
            throw new CustomServiceException("Invalid User ID format");
        }

        UUID newTaskId = UUID.fromString(taskId);
        UUID newUserId = UUID.fromString(userId);

        Task task = taskRepository.getById(newTaskId);

        if (task == null) {
            throw new CustomServiceException("Task cannot be found");
        }

        User user = userRepository.getById(newUserId);

        if (user == null) {
            throw new CustomServiceException("User cannot be found");
        }

        task.setAssignedTo(user);

        return taskRepository.save(task);
    }

    private boolean isValidUUID(String uuidStr) {
        try {
            UUID.fromString(uuidStr);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String changeStatus(String taskId, User user) {
        if (taskId == null || !isValidUUID(taskId)) {
            throw new CustomServiceException("Invalid Task ID format");
        }

        UUID newTaskId = UUID.fromString(taskId);

        Task task = taskRepository.getById(newTaskId);

        if (task == null) {
            throw new CustomServiceException("Task cannot be found");
        }

        if ((task.getAssignedTo() == null || !task.getAssignedTo().getId().equals(user.getId())) && user.getRole() != Role.ROLE_MANAGER) {
            throw new CustomServiceException("This employee cannot edit this task");
        }

        task.setStatus(Status.IN_PROGRESS);

        taskRepository.save(task);

        return "task status updated";
    }

}
