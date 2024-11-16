package salen.tasks.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import salen.tasks.entity.Task;
import salen.tasks.entity.dto.CommentDto;
import salen.tasks.entity.dto.TaskDto;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentDto.class})
public interface TaskMapper extends DefaultMapper<TaskDto, Task> {
    @Override
    @Mapping(target = "authorEmail", expression = "java(entity.getAuthor().getEmail())")
    @Mapping(target = "executorEmail", expression = "java(entity.getExecutor().getEmail())")
    TaskDto toDto(Task entity);
}
