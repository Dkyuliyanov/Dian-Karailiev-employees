package org.sirma.diankarailievemployees.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeePair {
    private int firstEmployeeId;
    private int secondEmployeeId;
    private int projectId;
    private long daysWorkedTogether;
}