CREATE TABLE IF NOT EXISTS PRICES (
    brand_id BIGINT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    price_list BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    priority INT NOT NULL,
    product_price DECIMAL(10,2) NOT NULL,
    curr VARCHAR(3) NOT NULL,
    PRIMARY KEY (PRICE_LIST)
);

INSERT INTO PRICES (brand_id, start_date, end_date, price_list, product_id, priority, product_price, curr) VALUES
(1, '2020-06-14T00:00:00', '2020-12-31T23:59:59', 1, 35455, 0, 35.50, 'EUR'),
(1, '2020-06-14T15:00:00', '2020-06-14T18:30:00', 2, 35455, 1, 25.45, 'EUR'),
(1, '2020-06-15T00:00:00', '2020-06-15T11:00:00', 3, 35455, 1, 30.50, 'EUR'),
(1, '2020-06-15T16:00:00', '2020-12-31T23:59:59', 4, 35455, 1, 38.95, 'EUR');
