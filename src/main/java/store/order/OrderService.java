package store.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import store.product.ProductController;
import store.product.ProductOut;

@Service
@Transactional
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductController productController;

    public Order create(Order order) {
        logger.debug("Creating order for account: {}", order.getIdAccount());

        order.setCreatedAt(LocalDateTime.now());
        order.setTotal(0.0);

        // Enrich items with product information and calculate totals
        List<OrderItem> enrichedItems = order.getItems().stream()
            .map(this::enrichItemWithProduct)
            .collect(Collectors.toList());

        order.setItems(enrichedItems);

        // Calculate order total
        double total = enrichedItems.stream()
            .mapToDouble(OrderItem::getSubtotal)
            .sum();
        order.setTotal(total);

        // Save order
        OrderModel savedOrderModel = orderRepository.save(new OrderModel(order));
        Order savedOrder = savedOrderModel.toDomain();
        savedOrder.setItems(enrichedItems);

        logger.debug("Order saved with ID: {}", savedOrder.getId());

        // Save items
        enrichedItems.forEach(item -> {
            OrderItemModel itemModel = new OrderItemModel(item, savedOrder.getId());
            OrderItemModel savedItem = orderItemRepository.save(itemModel);
            item.setId(savedItem.getId());
        });

        return savedOrder;
    }

    public List<Order> findAllByAccount(String idAccount) {
        logger.debug("Finding all orders for account: {}", idAccount);

        List<OrderModel> orderModels = orderRepository.findByIdAccount(idAccount);

        return orderModels.stream()
            .map(this::loadOrderWithItems)
            .collect(Collectors.toList());
    }

    public Order findById(String idAccount, String orderId) {
        logger.debug("Finding order {} for account: {}", orderId, idAccount);

        OrderModel orderModel = orderRepository
            .findByIdAndIdAccount(orderId, idAccount)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Order not found"
            ));

        return loadOrderWithItems(orderModel);
    }

    public void delete(String idAccount, String orderId) {
        logger.debug("Deleting order {} for account: {}", orderId, idAccount);

        OrderModel orderModel = orderRepository
            .findByIdAndIdAccount(orderId, idAccount)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Order not found"
            ));

        orderItemRepository.deleteByIdOrder(orderId);
        orderRepository.delete(orderModel);

        logger.debug("Order {} deleted successfully", orderId);
    }

    private OrderItem enrichItemWithProduct(OrderItem item) {
        ResponseEntity<ProductOut> response = productController
            .findProduct(item.getIdProduct());

        if (!response.hasBody()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Product not found: " + item.getIdProduct()
            );
        }

        ProductOut product = response.getBody();
        return OrderParser.enrichWithProduct(item, product);
    }

    private Order loadOrderWithItems(OrderModel orderModel) {
        Order order = orderModel.toDomain();

        List<OrderItem> items = orderItemRepository
            .findByIdOrder(order.getId())
            .stream()
            .map(OrderItemModel::toDomain)
            .map(this::loadProductForItem)
            .collect(Collectors.toList());

        order.setItems(items);
        return order;
    }

    private OrderItem loadProductForItem(OrderItem item) {
        ResponseEntity<ProductOut> response = productController
            .findProduct(item.getIdProduct());

        if (response.hasBody()) {
            item.setProduct(response.getBody());
        }

        return item;
    }

}