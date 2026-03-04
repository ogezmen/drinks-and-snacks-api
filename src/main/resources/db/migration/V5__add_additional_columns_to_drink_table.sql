ALTER TABLE drinks
    ADD COLUMN milliliters INT;

ALTER TABLE drinks
    ADD COLUMN alcohol_percentage FLOAT;

ALTER TABLE drinks
    ADD COLUMN packaging VARCHAR(20)
