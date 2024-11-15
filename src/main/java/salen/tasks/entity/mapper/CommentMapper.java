package salen.tasks.entity.mapper;

import org.mapstruct.Mapper;
import salen.tasks.entity.Comment;
import salen.tasks.entity.dto.CommentDto;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TaskMapper.class})
public interface CommentMapper extends DefaultMapper<CommentDto, Comment> {

}
