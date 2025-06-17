package org.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class StudentView {
    private int id;
    private String name;
    private String group;
    private boolean is_attended;
}
