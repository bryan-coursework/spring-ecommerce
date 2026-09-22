# spring-ecommerce

A small e-commerce domain model built to study **Spring Boot 4.1.1**, **Spring Data JPA**
and JPA relationship mappings. College / self-study project — everything is in English.

## Stack

| Concern            | Choice                                  |
| ------------------ | --------------------------------------- |
| Language / JDK     | Java 21                                 |
| Framework          | Spring Boot 4.1.1                       |
| Build              | Maven (wrapper included)                |
| Persistence        | Spring Data JPA + Hibernate             |
| Database           | MySQL 8.4 in Docker (never local)       |
| Boilerplate        | Lombok                                  |
| Config format      | `application.properties`                |

Starter dependencies: `web` (`spring-boot-starter-webmvc`), `data-jpa`, `lombok`,
`mysql`, the Boot test starters, and `spring-boot-docker-compose` (dev-time only).

## Domain model

```
Customer 1 ──< Order 1 ──< OrderItem >── 1 Product >──< Category
                   │
                   1
                   │
                   1
                Payment
```

| Entity      | Table            | Notes                                                                                  |
| ----------- | ---------------- | -------------------------------------------------------------------------------------- |
| `Category`  | `categories`     | Inverse side of the many-to-many with `Product`.                                       |
| `Product`   | `products`       | Owns the many-to-many with `Category` (`product_categories` join table).               |
| `Customer`  | `customers`      | Places orders. Named "Customer" (domain term) rather than "Client".                    |
| `Order`     | `orders`         | Belongs to one `Customer`; aggregates `OrderItem`s; has one `Payment`. `getTotal()` sums the lines. |
| `OrderItem` | `order_items`    | **Association entity** joining `Order` and `Product`. `@ManyToOne` to each side, composite key `OrderItemId` (`@EmbeddedId` + `@MapsId`). Stores `quantity` and a `unitPrice` snapshot so past orders are immune to later price changes. |
| `Payment`   | `payments`       | `@OneToOne` with `Order`, sharing its primary key (`@MapsId`).                          |

Enums: `OrderStatus` (`PENDING_PAYMENT`, `PAID`, `SHIPPED`, `DELIVERED`, `CANCELED`),
`PaymentMethod` (`CREDIT_CARD`, `DEBIT_CARD`, `BANK_SLIP`, `PIX`) — persisted as strings.

### Modelling choices (why it looks like this)

- **`OrderItem` is a real entity, not a plain `@ManyToMany`**, because the line carries
  its own data (`quantity`, `unitPrice`). That is exactly when you promote a join table
  to an entity.
- **Composite key `(order_id, product_id)`** via `@EmbeddedId` + `@MapsId`: a product
  appears at most once per order; add quantity instead of a second line.
- **Lombok on entities is deliberately minimal**: `@Getter @Setter @NoArgsConstructor`
  and `@EqualsAndHashCode(of = "id")`. No `@Data` / `@ToString` — they trigger lazy
  loading and recursion across associations.
- **`spring.jpa.open-in-view=false`** — the anti-pattern is off from day one.

## Running

Only Docker is required — no local MySQL, no local JDK juggling beyond Java 21.

```bash
./mvnw spring-boot:run
```

The `spring-boot-docker-compose` module reads `compose.yaml`, starts the **MySQL 8.4**
container (`spring-ecommerce-mysql`, port `3306`, volume `mysql-data`) and injects the
connection details automatically. `lifecycle-management=start-only` leaves the container
running after the app stops, so the next start is fast.

On every start Hibernate **drops and recreates** the schema (`ddl-auto=create`) and then
runs [`src/main/resources/import.sql`](src/main/resources/import.sql) — 5 rows per entity.

Stop the database when you are done:

```bash
docker compose down          # keep the data volume
docker compose down -v       # wipe it too
```

### Running the DB by hand

If you'd rather manage the container yourself (or run the packaged jar):

```bash
docker compose up -d
./mvnw spring-boot:run       # or: java -jar target/spring-ecommerce-0.0.1-SNAPSHOT.jar
```

The fallback datasource in `application.properties` points at `localhost:3306` with
user `ecommerce` / `ecommerce` (override with `DB_USERNAME` / `DB_PASSWORD`).

## Tests

```bash
./mvnw test
```

`SpringEcommerceApplicationTests` loads the full context; with Docker running, the
docker-compose module spins MySQL up for the test too.
