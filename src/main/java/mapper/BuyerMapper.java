package mapper;

import csv.BuyerRow;
import model.Order;
import model.Product;
import service.Buyer;

import java.util.List;

public class BuyerMapper implements Mapper<Buyer, BuyerRow> {
    @Override
    public BuyerRow map(Buyer from) {
        return new BuyerRow(
                from.getId(),
                from.getOrders().size(),
                getCaloriesAvg(from.getOrders()),
                getOrderPriceAvg(from.getOrders())
        );
    }

    private Double getOrderPriceAvg(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.products().stream())
                .mapToInt(Product::price)
                .average()
                .orElse(0.0);
    }

    private Double getCaloriesAvg(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.products().stream())
                .mapToInt(Product::calories)
                .average()
                .orElse(0.0);
    }


}
