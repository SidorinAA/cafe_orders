package job;

import csv.CsvRow;
import mapper.CashBoxMapper;
import service.CashBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class CashBoxStatsJob implements Runnable {

    private final List<CashBox> cashBoxes;
    private final Path cashBoxStatsPath;
    private final CashBoxMapper cashBoxMapper = new CashBoxMapper();

    public CashBoxStatsJob(List<CashBox> cashBoxes, Path cashBoxStatsPath) {
        this.cashBoxes = cashBoxes;
        this.cashBoxStatsPath = cashBoxStatsPath;
    }

    @Override
    public void run() {
        List<String> csvRows = cashBoxes.stream()
                .map(cashBoxMapper::map)
                .map(CsvRow::toCsvRow)
                .toList();
        try {
            Files.write(cashBoxStatsPath, csvRows, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
