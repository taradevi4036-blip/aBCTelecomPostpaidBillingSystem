# ABC Telecom Postpaid Billing System - Documentation

## Overview
The ABC Telecom Postpaid Billing System is a comprehensive Spring Boot application designed to manage customer accounts, track service usage, generate invoices, and process payments for telecom operators.

## Features

### Authentication & Authorization
- User registration with role-based access control
- JWT-based authentication
- Secure password hashing with BCrypt
- Role: CUSTOMER, ADMIN

### Customer Management
- Customer profile management
- Service management (activation, modification, suspension)
- Usage tracking and history
- Invoice generation and tracking
- Payment processing

### API Endpoints

#### Authentication
- `POST /api/register` - Register a new user
- `POST /api/login` - Authenticate user and get JWT token

#### Customer Management
- `GET /api/customers/{id}` - Get customer profile
- `PUT /api/customers/{id}` - Update customer profilemvbm

#### Services
- `GET /api/customers/{customerId}/services` - List customer services
- `POST /api/customers/{customerId}/services` - Add service (Admin only)

#### Usage
- `GET /api/services/{serviceId}/usage` - Get usage records
- `POST /api/services/{serviceId}/usage` - Record usage (Admin only)

#### Invoices
- `GET /api/customers/{customerId}/invoices` - Get customer invoices
- `POST /api/customers/{customerId}/invoices` - Generate invoice (Admin only)

#### Payments
- `GET /api/invoices/{invoiceId}/payments` - Get invoice payments
- `POST /api/invoices/{invoiceId}/payments` - Record payment

#### Admin Management
- `GET cc` - Get user details (Admin only)
- `PUT /api/admin/users/{id}` - Update user (Admin only)
- `DELETE /api/admin/users/{id}` - Delete user (Admin only)

## Database Schema

### Users Table
- user_id (PK)
- username (UNIQUE)
- password_hash
- email (UNIQUE)
- role
- created_at
- updated_at

### Customers Table
- customer_id (PK)
- user_id (FK)
- full_name
- address
- phone_number
- created_at
- updated_at

### Services Table
- service_id (PK)
- customer_id (FK)
- service_type
- start_date
- status
- created_at
- updated_at

### UsageRecords Table
- usage_id (PK)
- service_id (FK)
- usage_date
- usage_amount
- unit
- created_at

### Invoices Table
- invoice_id (PK)
- customer_id (FK)
- billing_period_start
- billing_period_end
- total_amount
- status
- created_at
- updated_at

### Payments Table
- payment_id (PK)
- invoice_id (FK)
- payment_date
- amount
- payment_method
- status
- created_at

## Setup Instructions

### Prerequisites
- Java 17
- Maven 3.8+
- PostgreSQL 12+ (or H2 for development)
- Docker & Docker Compose (for containerized deployment)

### Local Development Setup

1. **Clone/Extract the project:**
   ```bash
   cd ABC Telecom Postpaid Billing System/demo
   ```

2. **Configure database:**
   - Update `src/main/resources/application.properties` with your database credentials
   - For PostgreSQL:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/abc_telecom_db
     spring.datasource.username=postgres
     spring.datasource.password=your_password
     ```
   - Or use H2 for development (uncomment H2 configuration in properties)

3. **Build the project:**
   ```bash
   mvn clean install
   ```

4. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the API:**
   - Base URL: `http://localhost:8080`
   - Example: `POST http://localhost:8080/api/register`

### Docker Deployment

1. **Build and run with Docker Compose:**
   ```bash
   docker-compose up --build
   ```

2. **Application will be available at:**
   - API: `http://localhost:8080`
   - PostgreSQL: `localhost:5432`

3. **Stop the containers:**
   ```bash
   docker-compose down
   ```

4. **View logs:**
   ```bash
   docker-compose logs -f app
   ```

### Kubernetes Deployment

1. **Build Docker image:**
   ```bash
   docker build -t abc-telecom-billing:1.0 .
   ```

2. **Push to registry (optional):**
   ```bash
   docker tag abc-telecom-billing:1.0 your-registry/abc-telecom-billing:1.0
   docker push your-registry/abc-telecom-billing:1.0
   ```

3. **Create Kubernetes manifests:**
   - Deployment YAML
   - Service YAML
   - ConfigMap for application properties
   - PersistentVolume for database

4. **Deploy to cluster:**
   ```bash
   kubectl apply -f deployment.yaml
   kubectl apply -f service.yaml
   ```

## Testing the API

### 1. Register a new user
```bash
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "confirmPassword": "password123",
    "role": "customer"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

### 3. Get customer profile (use token from login response)
```bash
curl -X GET http://localhost:8080/api/customers/1 \
  -H "Authorization: Bearer <your_jwt_token>"
```

## Security Features

- Password encryption with BCrypt
- JWT token-based authentication
- Role-based access control (RBAC)
- CORS enabled for cross-origin requests
- SQL injection prevention through JPA
- CSRF protection via Spring Security

## Technology Stack

- **Framework:** Spring Boot 3.1.5
- **Language:** Java 17
- **Database:** PostgreSQL / H2
- **Authentication:** JWT
- **ORM:** Hibernate JPA
- **Build Tool:** Maven
- **Containerization:** Docker & Docker Compose
- **Container Orchestration:** Kubernetes (optional)

## Project Structure

```
demo/
├── src/
│   ├── main/
│   │   ├── java/com/hcltech/
│   │   │   ├── controller/     # REST Controllers
│   │   │   ├── service/        # Business Logic
│   │   │   ├── repository/     # Data Access
│   │   │   ├── entity/         # JPA Entities
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── security/       # JWT & Security
│   │   │   ├── config/         # Configuration Classes
│   │   │   └── Main.java       # Application Entry Point
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
├── Dockerfile
└── docker-compose.yml
```

## Future Enhancements

1. Email notifications for invoices and payments
2. SMS alerts for service status changes
3. Advanced reporting and analytics
4. Payment gateway integration (Stripe, PayPal)
5. Real-time notification system (WebSockets)
6. API documentation with Swagger/OpenAPI
7. Performance optimization and caching
8. Audit logging
9. Multi-language support
10. Mobile app integration

## Troubleshooting

### Port already in use
```bash
# On Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# On Linux/Mac
lsof -i :8080
kill -9 <PID>
```

### Database connection issues
- Ensure PostgreSQL is running
- Check connection string in application.properties
- Verify database credentials
- For Docker, ensure network connectivity

### JWT token issues
- Ensure token is correctly formatted as "Bearer <token>"
- Check token expiration time
- Verify jwt.secret matches between token generation and validation

## Support

For issues and questions, contact the development team or create an issue in the repository.

## License

This project is proprietary to HCL Technologies.
