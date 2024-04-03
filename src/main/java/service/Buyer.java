package service;

import model.Order;
import model.Product;
import model.ProductFactory;
import model.ProductType;
import util.CafeConst;
import util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class Buyer implements Runnable {

    private static int idGenerator = 1;
    private final Integer id;
    private final List<Order> orders = new ArrayList<>();
    private final BlockingQueue<Order> allOrders;

    public Buyer(BlockingQueue<Order> allOrders) {
        this.id = idGenerator++;
        this.allOrders = allOrders;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int productNumber = RandomUtil.getCountProducts(CafeConst.MAX_PRODUCT_COUNT);
                List<Product> products = IntStream.range(0, productNumber)
                        .mapToObj(i -> getRandomProduct())
                        .toList();
                Order order = new Order(id, products);
                allOrders.put(order);
                orders.add(order);
                Thread.sleep(CafeConst.BUYER_WAIT_TIME * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Product getRandomProduct() {
        ProductType[] values = ProductType.values();
        int index = RandomUtil.getCountProducts(values.length);
        return ProductFactory.getProductsFactory(values[index]);
    }

    public Integer getId() {
        return id;
    }

    public List<Order> getOrders() {
        return orders;
    }
}
