package app.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Row {
    private final List<Field> fields = new ArrayList<>();
}