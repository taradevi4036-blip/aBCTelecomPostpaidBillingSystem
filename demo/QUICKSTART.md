# ABC Telecom Billing System - Quick Start Guide

## 5-Minute Startup

### Option 1: Run with Docker Compose (Easiest)

```bash
# Navigate to project directory
cd "ABC Telecom Postpaid Billing System/demo"

# Start all services (PostgreSQL + App)
docker-compose up -d

# Wait for services to start (~30 seconds)
# Access the API at http://localhost:8080
```

### Option 2: Run Locally with Maven (H2 Database)

```bash
# Navigate to project directory
cd "ABC Telecom Postpaid Billing System/demo"

# Build and run
mvn clean install
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Access the API at http://localhost:8080
# H2 Console at http://localhost:8080/h2-console
```

## Test the API

### 1. Register a User

```bash
curl -X POST http://localhost:8080/api/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_customer",
    "email": "john@example.com",
    "password": "Test@123",
    "confirmPassword": "Test@123",
    "role": "customer"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_customer",
    "password": "Test@123"
  }'
```

Response will include a JWT token. Copy the token value.

### 3. Get Customer Profile (Replace TOKEN with actual token)

```bash
curl -X GET http://localhost:8080/api/customers/1 \
  -H "Authorization: Bearer <TOKEN>"
```

## Using Postman

1. Import `ABC_Telecom_API.postman_collection.json` into Postman
2. Set variable `base_url` = `http://localhost:8080`
3. Use the pre-built requests to test all endpoints

## Project Structure

```
demo/
├── src/main/java/com/hcltech/
│   ├── controller/          # REST API endpoints
│   ├── service/             # Business logic
│   ├── repository/          # Database access
│   ├── entity/              # JPA entities
│   ├── dto/                 # Data transfer objects
│   ├── security/            # JWT & Security
│   ├── config/              # Spring configuration
│   └── Main.java            # Application entry point
├── src/main/resources/
│   └── application.properties
├── pom.xml                  # Maven dependencies
├── Dockerfile               # Docker build configuration
├── docker-compose.yml       # Multi-container setup
├── kubernetes-manifest.yaml # Kubernetes deployment
├── README.md                # Full documentation
└── DEPLOYMENT_GUIDE.md      # Deployment instructions
```

## Key Features Implemented

✅ User Registration with Role-Based Access  
✅ JWT Authentication  
✅ Customer Profile Management  
✅ Service Management (Add/View/Update)  
✅ Usage Tracking and History  
✅ Invoice Generation and Tracking  
✅ Payment Processing  
✅ Admin User Management  
✅ PostgreSQL Database Integration  
✅ Docker & Docker Compose Support  
✅ Kubernetes Ready with Auto-scaling  

## API Endpoints Summary

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| POST | /api/register | Register new user | No |
| POST | /api/login | User login | No |
| GET | /api/customers/{id} | Get customer profile | Yes |
| PUT | /api/customers/{id} | Update profile | Yes |
| GET | /api/customers/{id}/services | List services | Yes |
| POST | /api/customers/{id}/services | Add service | Yes (Admin) |
| GET | /api/services/{id}/usage | Get usage records | Yes |
| POST | /api/services/{id}/usage | Record usage | Yes (Admin) |
| GET | /api/customers/{id}/invoices | List invoices | Yes |
| POST | /api/customers/{id}/invoices | Generate invoice | Yes (Admin) |
| GET | /api/invoices/{id}/payments | List payments | Yes |
| POST | /api/invoices/{id}/payments | Record payment | Yes |
| GET | /api/admin/users/{id} | Get user details | Yes (Admin) |
| PUT | /api/admin/users/{id} | Update user | Yes (Admin) |
| DELETE | /api/admin/users/{id} | Delete user | Yes (Admin) |

## Technology Stack

- **Java 17** - Programming Language
- **Spring Boot 3.1.5** - Framework
- **Spring Security** - Authentication & Authorization
- **JWT** - Token-based Security
- **PostgreSQL/H2** - Database
- **Hibernate JPA** - ORM
- **Maven** - Build Tool
- **Docker** - Containerization
- **Kubernetes** - Orchestration
- **Lombok** - Code Generation

## Default Test Credentials

For development/testing purposes:

- **Username:** testuser
- **Password:** password123
- **Role:** customer

## Logs Location

- **Docker Compose:** `docker-compose logs -f app`
- **Kubernetes:** `kubectl logs -f deployment/abc-telecom-app -n abc-telecom`
- **Local:** Console output or `logs/application.log` (prod profile)

## Stopping Services

### Docker Compose
```bash
docker-compose down
```

### Kubernetes
```bash
kubectl delete -f kubernetes-manifest.yaml
```

## Next Steps

1. Review [README.md](README.md) for detailed documentation
2. Check [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) for deployment options
3. Customize configuration in `application-prod.properties`
4. Set up database backups and monitoring
5. Deploy to your Kubernetes cluster

## Troubleshooting

### Port 8080 already in use
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

### Docker container fails to start
```bash
docker-compose logs app
```

### Database connection refused
- Ensure PostgreSQL is running and accessible
- Verify credentials in application.properties
- Check database exists: `createdb abc_telecom_db`

## Support

For issues and detailed documentation:
- See [README.md](README.md)
- See [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md)
- Check application logs

---

**Happy Coding! 🚀**
