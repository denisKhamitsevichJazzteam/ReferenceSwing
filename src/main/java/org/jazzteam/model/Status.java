package org.jazzteam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    OPEN("Open"),
    IN_PROGRESS("In progress"),
    CLOSED("Closed");

    private final String displayName;

}
