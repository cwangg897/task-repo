create table payments(
                         id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         transaction_id VARCHAR(1000) comment "pg사마다 toss: payment key, 네이버:결제번호",
                         user_id BIGINT NOT NULL,
                         amount BIGINT NOT NULL,
                         order_id VARCHAR(100) NOT NULL,
                         status VARCHAR(50) NOT NULL DEFAULT 'READY',
                         pay_type VARCHAR(50) NOT NULL,
                         pay_gateway VARCHAR(50) NOT NULL,
                         fail_reason VARCHAR(100),
                         created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         approved_at timestamp
);