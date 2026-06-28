# Online Retail Management Platform

**Duration:** January 2018 – December 2018

## Description

A complete retail platform where customers can browse products, place orders, make payments, and track shipments. The admin portal supports inventory management, promotions, and order fulfillment.

## Technologies

- **Backend:** Java 8, Spring Boot 2.0.4, Spring Data JPA, MySQL 5.7
- **Frontend:** Angular 5 (TypeScript), Bootstrap 4
- **Infrastructure:** Docker, Docker Compose, Jenkins CI/CD Pipeline
- **Tools:** Maven, Git, REST APIs

## Architecture

```
Online-Retail-Management-Platform/
├── backend/                     # Spring Boot REST API
│   └── src/main/java/com/retail/platform/
│       ├── model/               # JPA Entities (10)
│       ├── repository/          # Spring Data JPA Repositories (9)
│       ├── service/             # Business Logic Services (7)
│       ├── controller/          # REST Controllers (7)
│       ├── dto/                 # Data Transfer Objects (5)
│       └── config/              # CorsConfig, DataInitializer
├── frontend/                    # Angular 5 SPA
│   └── src/app/
│       ├── models/              # TypeScript Interfaces
│       ├── services/            # HTTP Services (5)
│       ├── guards/              # AuthGuard
│       ├── interceptors/        # AuthInterceptor
│       └── components/          # UI Components (14)
├── database/                    # MySQL DDL Schema
├── Dockerfile.backend
├── Dockerfile.frontend
├── docker-compose.yml
├── nginx.conf
└── Jenkinsfile
```

## Features

### Customer Portal
- Product browsing with search and category filters
- Product detail pages with stock information
- Shopping cart management (add, update, remove)
- Secure checkout with address and payment selection
- Promo code / discount support
- Order history with status tracking
- Real-time shipment tracking by tracking number

### Admin Portal
- Dashboard with revenue, order, product, and customer stats
- Low stock alerts
- Full product CRUD (create, update, deactivate)
- Order lifecycle management (confirm → ship → deliver)
- Promotion code management

## Setup

### Prerequisites
- Java 8+, Maven 3.5+
- Node.js 8+, Angular CLI 1.7.4
- MySQL 5.7 or Docker + Docker Compose

### Local Development

**Backend:**
```bash
cd backend
mvn spring-boot:run
```

**Frontend:**
```bash
cd frontend
npm install
npm start
```

### Docker Deployment
```bash
# Build backend JAR first
cd backend && mvn clean package -DskipTests && cd ..

# Start all services
docker-compose up --build -d
```

Access: http://localhost (frontend), http://localhost:8080 (backend API)

### Demo Data
On first startup, the system seeds:
- 4 product categories
- 11 sample products
- 1 demo customer (john.doe@example.com / password123)
- 2 promo codes: `WELCOME10` (10% off), `FLAT100` (Rs. 100 flat)

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register customer |
| POST | /api/auth/login | Login |
| GET | /api/products | List products |
| GET | /api/products/{id} | Product detail |
| GET | /api/cart | Get cart |
| POST | /api/cart/add | Add to cart |
| POST | /api/orders | Place order |
| GET | /api/orders | My orders |
| GET | /api/shipments/track/{number} | Track shipment |
| GET | /api/admin/dashboard | Admin stats |
