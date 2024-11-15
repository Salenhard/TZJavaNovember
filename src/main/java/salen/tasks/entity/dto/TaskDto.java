package salen.tasks.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    @NotBlank(message = "title is mandatory")
    private String title;
    @NotBlank(message = "description is mandatory")
    private String description;
    @NotBlank
    private String status;
    @NotBlank
    private String priority;
    private Long authorId;
    @NotNull
    private Long executorId;

}
