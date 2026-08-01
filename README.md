# Full Stack Web Application — Java & Spring Boot

A complete, ready-to-run full-stack application built with **Java, Spring Boot, Hibernate/JPA, Spring Security, and Thymeleaf**, following layered architecture (Controller → Service → Repository).

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.3 |
| Security | Spring Security (session-based auth, BCrypt, role-based access) |
| ORM / Database | Hibernate / Spring Data JPA, MySQL (default) or PostgreSQL |
| Frontend | Thymeleaf, HTML5, CSS3, JavaScript |
| Build | Maven |
| Dev/testing DB | H2 (in-memory) via the `dev` profile |

## Features implemented

- **Authentication & Authorization** — session-based login/registration, role-based access control (`ROLE_ADMIN` / `ROLE_USER`), BCrypt password hashing
- **CRUD Operations** — full Product management (create, read, update, delete) both through the web UI and a JSON REST API
- **Database Management** — Hibernate/JPA entities (`User`, `Product`) with automatic schema generation
- **Responsive UI** — Thymeleaf templates with a shared navbar fragment, clean CSS, mobile-friendly layout
- **Admin Dashboard** — user list, enable/disable/delete users, total user & product counts
- **Form Validation & Error Handling** — Jakarta Bean Validation on all forms and the REST API, global exception handler returning JSON errors, custom error page for the web UI
- **RESTful API** — `/api/products` endpoints for JSON-based integrations
- **Layered Architecture** — Controller / Service / Repository separation, DTOs for input, entities for persistence

## Project structure

```
fullstack-app/
├── pom.xml
├── src/main/java/com/example/fullstackapp/
│   ├── FullstackAppApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java        # Spring Security rules, BCrypt, form login
│   │   └── DataInitializer.java       # Seeds a default admin account
│   ├── controller/
│   │   ├── HomeController.java
│   │   ├── AuthController.java        # Login/registration pages
│   │   ├── ProductController.java     # Web UI CRUD for products
│   │   ├── AdminController.java       # Admin dashboard + user management
│   │   └── api/ProductRestController.java  # JSON REST API
│   ├── entity/            # User, Role, Product (JPA entities)
│   ├── repository/        # Spring Data JPA repositories
│   ├── service/            # Business logic (interfaces + impls)
│   ├── dto/                 # Validated input objects for forms/API
│   └── exception/          # Custom exceptions + global handler
├── src/main/resources/
│   ├── application.properties       # MySQL config (default) + PostgreSQL notes
│   ├── application-dev.properties   # H2 in-memory config for instant local testing
│   ├── templates/                    # Thymeleaf views
│   └── static/css, static/js        # Styling and small UI scripts
└── src/test/java/...                 # Spring context load test
```

## Quick start (PostgreSQL via pgAdmin)

The app now runs against **PostgreSQL by default** (not the H2 dev profile), so accounts and products you create actually persist.

1. Open **pgAdmin**, connect to your local PostgreSQL server.
2. Right-click **Databases → Create → Database...** and name it `fullstack_app_db`.
3. The app is already configured to connect with:
   ```
   host: localhost
   port: 5432
   database: fullstack_app_db
   username: postgres
   password: root
   ```
   If your local Postgres uses a different username/password, edit `src/main/resources/application.properties` accordingly.
4. Run the app:
   ```bash
   cd fullstack-app
   mvn spring-boot:run
   ```
   Hibernate will auto-create the `users` and `products` tables on startup (`spring.jpa.hibernate.ddl-auto=update`). You can verify this by refreshing the `fullstack_app_db` → Schemas → public → Tables list in pgAdmin.
5. Open **http://localhost:8080**

A default admin account (`admin` / `Admin@123`) is created automatically on first run and will now persist across restarts since it's stored in PostgreSQL.

### Prefer to test without installing PostgreSQL first?

Set `spring.profiles.active=dev` in `application.properties` (uncomment that line) to fall back to the in-memory H2 database described further down — good for a quick look, but data won't be saved between restarts.

## What's new in the UI

- **Public storefront** — the landing page (`/`) now shows the full product catalog to anyone, logged in or not. Guests see a "Login to buy" prompt on each product; logged-in users get a "View" link through to product details.
- **MrShop branding** — animated gradient title, floating background blobs, and glassmorphism cards throughout.
- **3D product cards** — subtle tilt-on-hover effect with a light "shine" sweep animation.
- **Animated login/register** — glass-style cards with entrance animations and a glowing primary button.
- Only Login and Register are offered as calls-to-action on the landing page (the old feature-list section was removed per request).



## Web pages

| URL | Access | Description |
|---|---|---|
| `/` | Public | Landing page — MrShop storefront, product catalog, login/register buttons only |
| `/home` | Authenticated | Personalized MrShop home with quick links |
| `/register` | Public | Create a new account |
| `/login` | Public | Log in |
| `/products` | Public (view) / ADMIN (manage) | List & search products; anyone can view, only admins see add/edit/delete |
| `/products/new`, `/products/edit/{id}` | ADMIN only | Create/edit products |
| `/products/{id}` | Public | View a single product |
| `/admin/dashboard` | ADMIN only | Manage users, view stats |

## REST API

All endpoints require an authenticated session (log in through the browser first, or use a cookie-aware HTTP client).

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/products` | List all products (optional `?keyword=` search) |
| GET | `/api/products/{id}` | Get a single product |
| POST | `/api/products` | Create a product (JSON body) |
| PUT | `/api/products/{id}` | Update a product |
| DELETE | `/api/products/{id}` | Delete a product |

Example request body for `POST`/`PUT`:
```json
{
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "price": 19.99,
  "quantity": 150,
  "category": "Electronics"
}
```

## Security notes

- Passwords are hashed with **BCrypt (strength 12)** — plaintext passwords are never stored.
- Role-based access control is enforced at the security-filter-chain level (`hasAuthority("ROLE_ADMIN")`), not just hidden in the UI.
- CSRF protection is enabled by default for all form submissions (the REST API under `/api/**` is exempted since it's intended for programmatic JSON access).
- Generic, non-revealing error messages are used where relevant to avoid leaking account existence.
- For a public production deployment, also consider: HTTPS, a stronger session timeout policy, and restricting the REST API with its own token-based auth (e.g. JWT) if it will be called by external clients rather than the browser.

## Requirements

- Java 17+
- Maven 3.8+
- (Optional) MySQL 8+ or PostgreSQL 13+ if you don't want to use the built-in H2 dev database

## Notes

- This project was generated as a working reference implementation. Before building it, you'll need an internet connection so Maven can download the dependencies listed in `pom.xml` (Spring Boot, Hibernate, Spring Security, database drivers, Lombok, etc.) — Maven cannot resolve these offline the first time.
- Lombok is used to reduce boilerplate (`@Data`, `@Builder`, etc.). Most IDEs (IntelliJ, Eclipse, VS Code) need the Lombok plugin/annotation processing enabled — this is usually automatic with recent versions.
