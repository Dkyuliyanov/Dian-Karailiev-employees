package org.sirma.diankarailievemployees.services;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class MultipleDateFormatConverter extends AbstractBeanField<LocalDate, String> {
    private static final List<DateTimeFormatter> SUPPORTED_FORMATS = Arrays.asList(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("MM-dd-yyyy"),
            DateTimeFormatter.ofPattern("yyyy.MM.dd")
    );

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException {
        if (value == null || value.isEmpty() || "NULL".equalsIgnoreCase(value)) {
            log.info("Received 'NULL' or empty date value, setting date to current date: {}", LocalDate.now());
            return LocalDate.now();
        }

        log.info("Attempting to parse date value: {}", value);

        return SUPPORTED_FORMATS.stream()
                .map(formatter -> tryParseDate(value, formatter))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Unparseable date or unsupported date format: \"{}\".", value);
                    return new CsvDataTypeMismatchException(value, LocalDate.class,
                            "Unparseable date or unsupported date format: " + value);
                });
    }

    private LocalDate tryParseDate(String value, DateTimeFormatter formatter) {
        try {
            return LocalDate.parse(value, formatter);
        } catch (DateTimeParseException e) {
            log.debug("Failed to parse date value: \"{}\" with format: {}", value, formatter);
            return null;
        }
    }
}
