package store.order;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class OrderResource implements OrderController {

    @Autowired
    private OrderService orderService;

    @Override
    public ResponseEntity<OrderOut> create(String idAccount, OrderIn orderIn) {
        Order order = OrderParser.toOrder(orderIn, idAccount);
        Order created = orderService.create(order);
        
        return ResponseEntity
            .created(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri())
            .body(OrderParser.toOrderOut(created));
    }

    @Override
    public ResponseEntity<List<OrderOut>> findAll(String idAccount) {
        List<Order> orders = orderService.findAllByAccount(idAccount);
        
        List<OrderOut> ordersOut = orders.stream()
            .map(OrderParser::toOrderOut)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(ordersOut);
    }

    @Override
    public ResponseEntity<OrderOut> findById(String idAccount, String id) {
        Order order = orderService.findById(idAccount, id);
        
        return ResponseEntity.ok(OrderParser.toOrderOut(order));
    }

    @Override
    public ResponseEntity<Void> delete(String idAccount, String id) {
        orderService.delete(idAccount, id);
        
        return ResponseEntity.noContent().build();
    }

}