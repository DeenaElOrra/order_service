package store.order;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import store.product.ProductOut;

public class OrderParser {

    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Order toOrder(OrderIn in, String idAccount) {
        if (in == null) return null;

        List<OrderItem> items = in.items() == null ? List.of() :
            in.items().stream()
                .map(OrderParser::toOrderItem)
                .collect(Collectors.toList());

        return Order.builder()
            .idAccount(idAccount)
            .items(items)
            .build();
    }

    public static OrderItem toOrderItem(OrderItemIn in) {
        if (in == null) return null;

        return OrderItem.builder()
            .idProduct(in.idProduct())
            .quantity(in.quantity())
            .build();
    }

    public static OrderOut toOrderOut(Order order) {
        if (order == null) return null;

        List<OrderItemOut> itemsOut = order.getItems() == null ? List.of() :
            order.getItems().stream()
                .map(OrderParser::toOrderItemOut)
                .collect(Collectors.toList());

        return OrderOut.builder()
            .id(order.getId())
            .idAccount(order.getIdAccount())
            .date(order.getCreatedAt() != null ? 
                DATE_FORMATTER.format(order.getCreatedAt()) : null)
            .items(itemsOut)
            .total(order.getTotal())
            .build();
    }

    public static OrderItemOut toOrderItemOut(OrderItem item) {
        if (item == null) return null;

        return OrderItemOut.builder()
            .id(item.getId())
            .product(item.getProduct())
            .quantity(item.getQuantity())
            .subtotal(item.getSubtotal())
            .build();
    }

    public static OrderItem enrichWithProduct(OrderItem item, ProductOut product) {
        if (item == null) return null;

        item.setProduct(product);
        item.setPrice(product.price());
        item.setSubtotal(item.getQuantity() * product.price());
        
        return item;
    }

}