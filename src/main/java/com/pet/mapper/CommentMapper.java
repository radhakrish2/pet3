package com.pet.mapper;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.pet.entity.Comment;
import com.pet.entity.dto.CommentDTO;

@Component
public class CommentMapper {

    // Convert Comment Entity to CommentDTO
    public static CommentDTO toDTO(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setPetId(comment.getPet().getId()); // Assuming Pet is an entity and has an ID
        dto.setUserId(comment.getUser().getId()); // Assuming User is an entity and has an ID
        dto.setContent(comment.getContent());
     
        return dto;
    }

    // Convert CommentDTO to Comment Entity
    public static Comment toEntity(CommentDTO dto) {
        if (dto == null) {
            return null;
        }
        Comment comment = new Comment();
        // Assuming that the Pet and User entities have been fetched or set from elsewhere
        comment.setId(dto.getId());
        // comment.setPet(fetchPetById(dto.getPetId()));
        // comment.setUser(fetchUserById(dto.getUserId()));
        comment.setContent(dto.getContent());
      
        return comment;
    }

    // Convert List of Comment Entities to List of CommentDTOs
    public static List<CommentDTO> toDTOList(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Convert List of CommentDTOs to List of Comment Entities
    public static List<Comment> toEntityList(List<CommentDTO> dtos) {
        return dtos.stream()
                .map(CommentMapper::toEntity)
                .collect(Collectors.toList());
    }
}

