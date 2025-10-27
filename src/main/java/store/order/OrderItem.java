package store.order;

import lombok.Builder;
import lombok.Data;
import store.product.ProductOut;

@Builder
@Data
public class OrderItem {
    private String id;
    private String idProduct;
    private ProductOut product;
    private Integer quantity;
    private Double price;
    private Double subtotal;
}