package app.tree.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Constraint {

    @EqualsAndHashCode.Include
    private ConstraintType constraintType;

    @Override
    public String toString() {
        final String[] constraintParts = this.constraintType.toString().split("_");

        for (int i = 0; i < constraintParts.length; i++) {
            if (constraintParts[i].length() > 0) {
                String constraint = constraintParts[i].substring(0, 1).toUpperCase();
                if (constraintParts[i].length() > 1) {
                    constraint += constraintParts[i].substring(1).toLowerCase();
                }
                constraintParts[i] = constraint;
            }
        }

        return String.join(" ", constraintParts);
    }
}