package org.sirma.diankarailievemployees.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import java.io.Reader;
import java.util.List;

@Service
@RequestScope
@Slf4j
public class CsvToPojo {
    public <T> List<T> parseCsv(Reader reader, Class<T> type) {
        log.info("Creating request scoped CSV parser.");
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                .withType(type)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        return csvToBean.parse();
    }
}
