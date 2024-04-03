package mapper;

import csv.CashBoxRow;
import model.Order;
import model.Product;
import service.CashBox;

import java.util.List;

public class CashBoxMapper implements Mapper<CashBox, CashBoxRow> {
    @Override
    public CashBoxRow map(CashBox from) {
        return new CashBoxRow(
                from.getId(),
                from.getOrders().size(),
                getOrderPriceSuv(from.getOrders())
        );
    }

    private Integer getOrderPriceSuv(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.products().stream())
                .mapToInt(Product::price)
                .sum();
    }


}
