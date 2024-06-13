package org.sirma.diankarailievemployees.services;

import org.sirma.diankarailievemployees.model.WorkEntry;
import org.sirma.diankarailievemployees.model.EmployeePair;

import java.util.List;

public interface EmployeePairService {

    List<EmployeePair> findAllWorkingPairs(List<WorkEntry> workEntries);

}
