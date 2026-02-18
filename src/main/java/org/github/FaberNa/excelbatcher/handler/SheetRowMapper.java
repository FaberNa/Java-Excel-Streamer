package org.github.faberNa.excelbatcher.handler;


import org.github.faberNa.excelbatcher.config.BatchRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public interface SheetRowMapper<T> {
    T map(BatchRow row);
     static final Logger log = LoggerFactory.getLogger(SheetRowMapper.class);


    default Optional<String> cell(List<String> cells, int idx) {
        log.debug("Extracting cell at index {} from cells={}", idx, cells);
        return Optional.ofNullable(cells)
                .filter(c -> idx >= 0 && idx < c.size())
                .map(c -> c.get(idx))
                .map(String::trim)
                .filter(s -> !s.isEmpty());
    }
}

