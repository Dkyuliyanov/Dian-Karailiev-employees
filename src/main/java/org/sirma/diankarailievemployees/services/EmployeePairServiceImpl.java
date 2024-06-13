package org.sirma.diankarailievemployees.services;

import lombok.extern.slf4j.Slf4j;
import org.sirma.diankarailievemployees.model.WorkEntry;
import org.sirma.diankarailievemployees.model.EmployeePair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class EmployeePairServiceImpl implements EmployeePairService {
    private static final long ZERO_OVERLAP = 0L;

    @Override
    public List<EmployeePair> findAllWorkingPairs(List<WorkEntry> workEntries) {
        log.info("Processing {} entries to find all working pairs.", workEntries.size());

        Map<Integer, List<WorkEntry>> projectWorkMap = workEntries.stream()
                .collect(Collectors.groupingBy(WorkEntry::getProjectId));

        log.debug("Projects loaded: {}", projectWorkMap.keySet());

        List<EmployeePair> allPairs = new ArrayList<>();

        projectWorkMap.forEach((projectId, woEntry) -> {
            log.info("Processing project ID {}: {} entries", projectId, woEntry.size());
            List<EmployeePair> overlaps = findOverlaps(woEntry);
            log.info("Found {} overlaps in project ID {}", overlaps.size(), projectId);
            allPairs.addAll(overlaps);
        });

        log.info("Total overlaps found: {}", allPairs.size());
        return allPairs;
    }

    private List<EmployeePair> findOverlaps(List<WorkEntry> workEntries) {
        log.debug("Finding overlaps for {} work entries", workEntries.size());
        return IntStream.range(0, workEntries.size())
                .boxed()
                .flatMap(i -> IntStream.range(i + 1, workEntries.size())
                        .mapToObj(j -> {
                            log.trace("Comparing entries at index {} and {}", i, j);
                            return createEmployeePairIfOverlap(workEntries.get(i), workEntries.get(j));
                        })
                        .filter(Optional::isPresent)
                        .map(Optional::get))
                .peek(pair -> log.debug("Overlap found: {}", pair))
                .collect(Collectors.toList());
    }

    private Optional<EmployeePair> createEmployeePairIfOverlap(WorkEntry entry1, WorkEntry entry2) {
        log.trace("Checking potential overlap between Employee {} and Employee {}", entry1.getEmployeeId(), entry2.getEmployeeId());
        LocalDate start1 = entry1.getDateFrom();
        LocalDate end1 = entry1.getDateTo();
        LocalDate start2 = entry2.getDateFrom();
        LocalDate end2 = entry2.getDateTo();

        if (doesOverlap(start1, end1, start2, end2)) {
            long overlapDays = calculateOverlapDays(start1, end1, start2, end2);
            if (overlapDays > ZERO_OVERLAP) {
                log.info("Overlap confirmed for {} days between Employee {} and Employee {} on Project {}", overlapDays, entry1.getEmployeeId(), entry2.getEmployeeId(), entry1.getProjectId());
                return Optional.of(new EmployeePair(
                        entry1.getEmployeeId(),
                        entry2.getEmployeeId(),
                        entry1.getProjectId(),
                        overlapDays
                ));
            }
        }
        return Optional.empty();
    }

    private static boolean doesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        boolean result = !end1.isBefore(start2) && !start1.isAfter(end2);
        log.trace("Overlap check between dates {} - {} and {} - {}: {}", start1, end1, start2, end2, result);
        return result;
    }

    private static long calculateOverlapDays(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        LocalDate overlapStart = start1.isAfter(start2) ? start1 : start2;
        LocalDate overlapEnd = end1.isBefore(end2) ? end1 : end2;
        long days = overlapStart.isBefore(overlapEnd) ? overlapEnd.toEpochDay() - overlapStart.toEpochDay() : ZERO_OVERLAP;
        log.trace("Calculated overlap days: {}", days);
        return days;
    }
}
