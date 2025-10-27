CREATE TABLE orders (
    id_order VARCHAR(36) NOT NULL,
    id_account VARCHAR(36) NOT NULL,
    dt_created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    vl_total DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_order PRIMARY KEY (id_order)
);
