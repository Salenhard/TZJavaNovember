package salen.tasks.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("author")
    private String authorEmail;
    @JsonProperty("executor")
    private String executorEmail;
    @NotNull(message = "executor is mandatory")
    @JsonProperty(value = "executorId", access = JsonProperty.Access.WRITE_ONLY)
    private Long executorId;

}
