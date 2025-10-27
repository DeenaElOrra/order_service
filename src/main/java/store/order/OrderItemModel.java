package store.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemModel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "id_order", nullable = false)
    private String idOrder;

    @Column(name = "id_product", nullable = false)
    private String idProduct;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    public OrderItemModel(OrderItem item, String idOrder) {
        this.id = item.getId();
        this.idOrder = idOrder;
        this.idProduct = item.getIdProduct();
        this.quantity = item.getQuantity();
        this.price = item.getPrice();
        this.subtotal = item.getSubtotal();
    }

    public OrderItem toDomain() {
        return OrderItem.builder()
            .id(this.id)
            .idProduct(this.idProduct)
            .quantity(this.quantity)
            .price(this.price)
            .subtotal(this.subtotal)
            .build();
    }

}