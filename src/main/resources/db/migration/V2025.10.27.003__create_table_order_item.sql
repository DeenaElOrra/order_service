CREATE TABLE order_item (
    id_order_item VARCHAR(36) NOT NULL,
    id_order VARCHAR(36) NOT NULL,
    id_product VARCHAR(36) NOT NULL,
    qt_quantity INTEGER NOT NULL,
    vl_price DOUBLE PRECISION NOT NULL,
    vl_subtotal DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_order_item PRIMARY KEY (id_order_item),
    CONSTRAINT fk_order_item_order FOREIGN KEY (id_order) 
        REFERENCES orders(id_order) ON DELETE CASCADE
);