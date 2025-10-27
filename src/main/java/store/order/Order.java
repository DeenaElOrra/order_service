package store.order;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Order {
    private String id;
    private String idAccount;
    private LocalDateTime createdAt;
    private List<OrderItem> items;
    private Double total;
}