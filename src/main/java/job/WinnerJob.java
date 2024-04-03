package job;

import csv.BuyerRow;
import csv.CashBoxRow;
import csv.CsvRow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Comparator.comparing;

public class WinnerJob implements Runnable {

    private final Path buyerStatsPath;
    private final Path cashBoxStatsPath;

    public WinnerJob(Path buyerStatsPath, Path cashBoxStatsPath) {
        this.buyerStatsPath = buyerStatsPath;
        this.cashBoxStatsPath = cashBoxStatsPath;
    }

    @Override
    public void run() {
        try {
            determineBuyerWinner();
            determineCashBoxWinner();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void determineCashBoxWinner() throws IOException {
        Files.readAllLines(cashBoxStatsPath).stream()
                .map(line -> line.split(CsvRow.COLUMN_SEPARATOR))
                .map(CashBoxRow::new)
                .max(comparing(cashBoxRow -> cashBoxRow.orderPriceSum() / cashBoxRow.ordersNumber()))
                .ifPresent(cashBoxRow -> System.out.println("CashBox winner: " + cashBoxRow.id()));
    }

    private void determineBuyerWinner() throws IOException {
        Files.readAllLines(buyerStatsPath).stream()
                .map(line -> line.split(CsvRow.COLUMN_SEPARATOR))
                .map(BuyerRow::new)
                .max(comparing(buyerRow -> buyerRow.ordersNumber() * buyerRow.orderPriceAvg()))
                .ifPresent(buyerRow -> System.out.println("Buyer winner: " + buyerRow.id()));
    }


}
