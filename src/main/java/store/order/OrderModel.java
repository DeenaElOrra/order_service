package store.order;

import java.time.LocalDateTime;

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
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "id_account", nullable = false)
    private String idAccount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "total", nullable = false)
    private Double total;

    public OrderModel(Order order) {
        this.id = order.getId();
        this.idAccount = order.getIdAccount();
        this.createdAt = order.getCreatedAt();
        this.total = order.getTotal();
    }

    public Order toDomain() {
        return Order.builder()
            .id(this.id)
            .idAccount(this.idAccount)
            .createdAt(this.createdAt)
            .total(this.total)
            .build();
    }

}