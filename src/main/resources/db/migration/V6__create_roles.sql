CREATE TABLE user_roles
(
    user_id UUID REFERENCES users (id) ON DELETE CASCADE,
    role    VARCHAR(50) NOT NULL,

    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role)
);