package org.jazzteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Todo {
    private String title;
    private String description;
    private String priorityName;
    private LocalDate creationDate;
    private LocalDate dueDate;
    private Status status;
}
