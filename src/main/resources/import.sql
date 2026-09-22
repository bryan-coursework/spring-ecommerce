-- Seed data reloaded on every startup.
--
-- Hibernate runs this file automatically after it (re)creates the schema,
-- because spring.jpa.hibernate.ddl-auto=create. Five rows per entity.
--
-- Keep every statement on a SINGLE line: Hibernate's default import-script
-- parser is line-based and does not understand multi-line statements.
-- Insertion order follows the foreign keys.

-- Categories -----------------------------------------------------------
insert into categories (id, name) values (1, 'Electronics');
insert into categories (id, name) values (2, 'Books');
insert into categories (id, name) values (3, 'Home & Kitchen');
insert into categories (id, name) values (4, 'Sports & Outdoors');
insert into categories (id, name) values (5, 'Toys & Games');

-- Products -------------------------------------------------------------
insert into products (id, name, description, price) values (1, 'Wireless Mouse', 'Ergonomic 2.4GHz wireless mouse with silent clicks', 79.90);
insert into products (id, name, description, price) values (2, 'Mechanical Keyboard', 'Compact 75% hot-swappable mechanical keyboard', 349.00);
insert into products (id, name, description, price) values (3, 'Clean Code', 'A Handbook of Agile Software Craftsmanship, by Robert C. Martin', 129.90);
insert into products (id, name, description, price) values (4, 'Yoga Mat', 'Non-slip 6mm TPE yoga mat with carrying strap', 89.90);
insert into products (id, name, description, price) values (5, 'Building Blocks Set', '500-piece creative building blocks set', 199.00);

-- Product <-> Category (many-to-many join table) ----------------------
insert into product_categories (product_id, category_id) values (1, 1);
insert into product_categories (product_id, category_id) values (2, 1);
insert into product_categories (product_id, category_id) values (3, 2);
insert into product_categories (product_id, category_id) values (4, 4);
insert into product_categories (product_id, category_id) values (4, 3);
insert into product_categories (product_id, category_id) values (5, 5);

-- Customers ----------------------------------------------------------
insert into customers (id, name, email, phone) values (1, 'Alice Silva', 'alice.silva@example.com', '+55 11 90000-0001');
insert into customers (id, name, email, phone) values (2, 'Bruno Costa', 'bruno.costa@example.com', '+55 21 90000-0002');
insert into customers (id, name, email, phone) values (3, 'Carla Nunes', 'carla.nunes@example.com', '+55 31 90000-0003');
insert into customers (id, name, email, phone) values (4, 'Diego Rocha', 'diego.rocha@example.com', '+55 41 90000-0004');
insert into customers (id, name, email, phone) values (5, 'Elena Dias', 'elena.dias@example.com', '+55 51 90000-0005');

-- Orders (table is "orders"; ORDER is the reserved word, ORDERS is not)
insert into orders (id, placed_at, status, customer_id) values (1, '2026-01-10 09:15:00', 'PAID', 1);
insert into orders (id, placed_at, status, customer_id) values (2, '2026-02-03 14:40:00', 'PAID', 2);
insert into orders (id, placed_at, status, customer_id) values (3, '2026-02-18 18:05:00', 'SHIPPED', 3);
insert into orders (id, placed_at, status, customer_id) values (4, '2026-03-01 11:22:00', 'DELIVERED', 1);
insert into orders (id, placed_at, status, customer_id) values (5, '2026-03-12 20:30:00', 'CANCELED', 4);

-- Order items (association entity, composite key order_id + product_id)
insert into order_items (order_id, product_id, quantity, unit_price) values (1, 1, 2, 79.90);
insert into order_items (order_id, product_id, quantity, unit_price) values (2, 2, 1, 349.00);
insert into order_items (order_id, product_id, quantity, unit_price) values (3, 3, 1, 129.90);
insert into order_items (order_id, product_id, quantity, unit_price) values (4, 4, 2, 89.90);
insert into order_items (order_id, product_id, quantity, unit_price) values (5, 5, 1, 199.00);

-- Payments (one-to-one, shares the order primary key) -----------------
-- Order 5 was paid and later canceled: the payment row still exists (refund pending).
insert into payments (order_id, method, amount, paid_at) values (1, 'CREDIT_CARD', 159.80, '2026-01-10 09:16:10');
insert into payments (order_id, method, amount, paid_at) values (2, 'PIX', 349.00, '2026-02-03 14:41:00');
insert into payments (order_id, method, amount, paid_at) values (3, 'BANK_SLIP', 129.90, '2026-02-19 08:00:00');
insert into payments (order_id, method, amount, paid_at) values (4, 'DEBIT_CARD', 179.80, '2026-03-01 11:23:30');
insert into payments (order_id, method, amount, paid_at) values (5, 'CREDIT_CARD', 199.00, '2026-03-12 20:31:05');
