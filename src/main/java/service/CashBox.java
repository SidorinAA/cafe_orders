package service;

import model.Order;
import util.CafeConst;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CashBox implements Runnable {

    private static int idGenerator = 1;

    private final Integer id;

    private final List<Order> orders = new ArrayList<>();

    private final BlockingQueue<Order> allOrders;

    public CashBox(BlockingQueue<Order> allOrders) {
        this.id = idGenerator++;
        this.allOrders = allOrders;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Order order = allOrders.take();
                Thread.sleep(order.products().size() * CafeConst.PRODUCT_TIME_COST * 1000L);
                orders.add(order);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
