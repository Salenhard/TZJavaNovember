package salen.tasks.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    @NotBlank(message = "comment should not be empty")
    private String value;
    private Long authorId;
    @NotNull
    private Long taskId;
}
