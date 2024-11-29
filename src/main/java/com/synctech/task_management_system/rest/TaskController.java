package com.synctech.task_management_system.rest;

import com.synctech.task_management_system.dto.CreateTaskRequest;
import com.synctech.task_management_system.entity.Task;
import com.synctech.task_management_system.entity.User;
import com.synctech.task_management_system.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/tasks")
    @PreAuthorize("hasRole('MANAGER')")
    public Task createTask(@Valid @RequestBody CreateTaskRequest taskRequest, @AuthenticationPrincipal User user) {
        // Handle the task creation logic
        return taskService.create(taskRequest, user);
    }

    @GetMapping("/tasks")
    public List<Task> getTasks(@AuthenticationPrincipal User user) {
        return taskService.getAll(user);
    }

    @PatchMapping("/tasks/{taskId}/{userId}")
    @PreAuthorize("hasRole('MANAGER')")
    public Task assignTask(@PathVariable String taskId, @PathVariable String userId) {
        return taskService.assignTask(taskId, userId);
    }

    @PatchMapping("/tasks/{taskId}")
    public String changeStatus(@PathVariable String taskId, @AuthenticationPrincipal User user) {
        return taskService.changeStatus(taskId, user);
    }
}
