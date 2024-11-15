package salen.tasks.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "tasks")
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private Priority priority;

    @OneToMany
    private List<Comment> comments;

    @OneToOne
    private User author;

    @OneToOne
    private User executor;
}
