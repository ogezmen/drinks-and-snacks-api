CREATE TABLE drinks
(
    id       UUID PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    store_id UUID REFERENCES stores (id) ON DELETE CASCADE
);