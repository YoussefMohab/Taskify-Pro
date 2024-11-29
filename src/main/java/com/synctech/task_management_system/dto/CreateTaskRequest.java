package com.synctech.task_management_system.dto;

import com.synctech.task_management_system.entity.Priority;
import com.synctech.task_management_system.validation.ValidEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

    @NotNull(message = "Title cannot be null")
    String title;

    @NotNull(message = "Description cannot be null")
    String description;

    @NotNull(message = "Priority cannot be null")
    @ValidEnum(enumClass = Priority.class, message = "Priority must be one of: HIGH, MEDIUM, or LOW")
    String priority;

    @NotNull(message = "Due date cannot be null")
    LocalDate dueDate;
}
