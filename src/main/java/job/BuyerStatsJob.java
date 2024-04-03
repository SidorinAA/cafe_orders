package job;

import csv.CsvRow;
import mapper.BuyerMapper;
import service.Buyer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class BuyerStatsJob implements Runnable {

    private final List<Buyer> buyers;
    private final Path buyerStatsPath;
    private final BuyerMapper buyerMapper = new BuyerMapper();

    public BuyerStatsJob(List<Buyer> buyers, Path buyerStatsPath) {
        this.buyers = buyers;
        this.buyerStatsPath = buyerStatsPath;
    }

    @Override
    public void run() {
        List<String> csvRows = buyers.stream()
                .map(buyerMapper::map)
                .map(CsvRow::toCsvRow)
                .toList();
        try {
            Files.write(buyerStatsPath, csvRows, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
