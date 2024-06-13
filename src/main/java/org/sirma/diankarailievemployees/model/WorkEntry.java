package org.sirma.diankarailievemployees.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;
import org.sirma.diankarailievemployees.services.MultipleDateFormatConverter;

import java.time.LocalDate;

@Data
public class WorkEntry{
    @CsvBindByName(column = "ProjectID")
    private Integer projectId;

    @CsvBindByName(column = "EmpID")
    private Integer employeeId;

    @CsvBindByName(column = "DateFrom")
    @CsvCustomBindByName(column = "DateFrom", converter = MultipleDateFormatConverter.class)
    private LocalDate dateFrom;

    @CsvCustomBindByName(column = "DateTo", converter = MultipleDateFormatConverter.class)
    private LocalDate dateTo;
}
