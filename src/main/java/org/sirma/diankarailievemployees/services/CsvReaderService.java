package org.sirma.diankarailievemployees.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sirma.diankarailievemployees.model.WorkEntry;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.Reader;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CsvReaderService {

    private final CsvToPojo csvToPojo;

    public List<WorkEntry> readAllWorkEntries(Reader reader) {
        try {
            log.info("Parsing CSV file.");
            return csvToPojo.parseCsv(reader, WorkEntry.class);
        } catch (Exception e) {
            log.error("Failed to read CSV file: {}", e.getMessage(), e);
            throw new ResponseStatusException(400, "Failed to read CSV file: " + e.getMessage(), e);
        }
    }
}
