package salen.tasks.entity.mapper;

import org.mapstruct.Mapper;
import salen.tasks.entity.Task;
import salen.tasks.entity.dto.CommentDto;
import salen.tasks.entity.dto.TaskDto;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentDto.class})
public interface TaskMapper extends DefaultMapper<TaskDto, Task> {
}
