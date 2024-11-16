package salen.tasks.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;

    @NotBlank(message = "comment should not be empty")
    private String value;

    @JsonProperty("author")
    private String authorName;
}
