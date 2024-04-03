package service;

import job.BuyerStatsJob;
import job.CashBoxStatsJob;
import job.WinnerJob;
import model.Order;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static util.CafeConst.*;

public class Cafe extends Thread {

    private final ScheduledExecutorService executorService;
    private final List<Buyer> buyers;
    private final List<CashBox> cashBoxes;
    private final BlockingQueue<Order> allOrders;

    public Cafe(int buyerNumber, int cashBoxesNumber) {
        this.executorService = Executors.newScheduledThreadPool(3);
        this.allOrders = new ArrayBlockingQueue<>(cashBoxesNumber * 10);
        this.buyers = createBuyers(buyerNumber);
        this.cashBoxes = createCashBoxes(cashBoxesNumber);
    }

    @Override
    public void run() {
        buyers.forEach(buyer -> new Thread(buyer).start());
        cashBoxes.forEach(cashBox -> new Thread(cashBox).start());

        var buyerPath = Path.of("resources", "buyers-stats.csv");
        executorService.scheduleAtFixedRate(new BuyerStatsJob(buyers, buyerPath), BUYER_STATS_JOB_PERIOD, BUYER_STATS_JOB_PERIOD, TimeUnit.SECONDS);

        var cashPath = Path.of("resources", "cashboxes-stats.csv");
        executorService.scheduleAtFixedRate(new CashBoxStatsJob(cashBoxes, cashPath), CASHBOX_STATS_JOB_PERIOD, CASHBOX_STATS_JOB_PERIOD, TimeUnit.SECONDS);

        executorService.scheduleAtFixedRate(new WinnerJob(buyerPath, cashPath), WINNER_STATS_JOB_PERIOD, WINNER_STATS_JOB_PERIOD, TimeUnit.SECONDS);

    }

    private List<CashBox> createCashBoxes(int cashBoxesNumber) {
        return IntStream.range(0, cashBoxesNumber)
                .mapToObj(i -> new CashBox(allOrders))
                .toList();
    }

    private List<Buyer> createBuyers(int buyerNumber) {
        return IntStream.range(0, buyerNumber)
                .mapToObj(i -> new Buyer(allOrders))
                .toList();

    }

}
