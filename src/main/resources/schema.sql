CREATE TABLE IF NOT EXISTS items
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255),
    category   VARCHAR(255),
    created_at TIMESTAMP,
    price      DOUBLE PRECISION,
    status     VARCHAR(255)
    );
