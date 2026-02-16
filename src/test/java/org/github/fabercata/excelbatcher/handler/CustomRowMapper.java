package org.github.fabercata.excelbatcher.handler;

import org.github.fabercata.excelbatcher.config.BatchRow;
import org.github.fabercata.excelbatcher.model.MyCustomModel;
import org.github.fabercata.excelbatcher.util.CellParsers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class CustomRowMapper implements SheetRowMapper<MyCustomModel>{
    private final int COL_DATE;
    private final int COL_AMOUNT;
    private final int COL_QUANTITY;
    private final int COL_NAME;
    private final int COL_ACTIVE;
    private final DateTimeFormatter dateFormatter;


    public CustomRowMapper(int date, int amount, int quantity, int active,int name) {
        this.COL_DATE = date;
        this.COL_AMOUNT = amount;
        this.COL_QUANTITY = quantity;
        this.COL_NAME = name;
        this.COL_ACTIVE = active;
        this.dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE; // adattare se necessario
    }

    @Override
    public MyCustomModel map(BatchRow row) {
        List<String> cells = row.cells();

        Optional<LocalDate> date =
                cell(cells, COL_DATE)
                        .flatMap(v -> CellParsers.parseDate(v, dateFormatter));

        Optional<BigDecimal> amount =
                cell(cells, COL_AMOUNT)
                        .flatMap(CellParsers::parseBigDecimal);

        Optional<Integer> quantity =
                cell(cells, COL_QUANTITY)
                        .flatMap(CellParsers::parseInt);

        Optional<Boolean> active =
                cell(cells, COL_ACTIVE)
                        .flatMap(CellParsers::parseBoolean);

        Optional<String> name =
                cell(cells, COL_NAME);

        return MyCustomModel.builder()
                .date(date.orElse(null))
                .amount(amount.orElse(null))
                .quantity(quantity.orElse(0))
                .active(active.orElse(false))
                .name(name.orElse(null))
                .build();
    }
}
