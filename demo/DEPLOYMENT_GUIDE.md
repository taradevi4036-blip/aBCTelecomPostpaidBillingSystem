# ABC Telecom Billing System - Deployment Guide

## Table of Contents
1. [Local Development Setup](#local-development-setup)
2. [Docker Deployment](#docker-deployment)
3. [Kubernetes Deployment](#kubernetes-deployment)
4. [Production Considerations](#production-considerations)
5. [Monitoring and Logging](#monitoring-and-logging)

## Local Development Setup

### Prerequisites
- Java 17 or higher
- Maven 3.8+
- PostgreSQL 12+ (optional, H2 can be used for dev)
- Git

### Steps

1. **Clone/Extract the project**
   ```bash
   cd "ABC Telecom Postpaid Billing System/demo"
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run locally with H2 (in-memory database)**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
   ```

4. **Or run with PostgreSQL**
   - Install and start PostgreSQL
   - Create database: `createdb abc_telecom_db`
   - Update `application-dev.properties` with your credentials
   - Run: `mvn spring-boot:run`

5. **Access the application**
   - API Base URL: `http://localhost:8080`
   - H2 Console (dev): `http://localhost:8080/h2-console`

## Docker Deployment

### Single Container

1. **Build the Docker image**
   ```bash
   docker build -t abc-telecom-billing:1.0 .
   ```

2. **Run the container**
   ```bash
   docker run -d \
     --name abc-telecom-app \
     -p 8080:8080 \
     -e SPRING_PROFILES_ACTIVE=prod \
     -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/abc_telecom_db \
     -e SPRING_DATASOURCE_USERNAME=postgres \
     -e SPRING_DATASOURCE_PASSWORD=postgres \
     abc-telecom-billing:1.0
   ```

### Docker Compose (Recommended for local deployment)

1. **Start all services**
   ```bash
   docker-compose up -d
   ```

2. **View logs**
   ```bash
   docker-compose logs -f app
   ```

3. **Stop all services**
   ```bash
   docker-compose down
   ```

4. **Verify services are running**
   ```bash
   docker-compose ps
   ```

### Docker Commands Reference

```bash
# Build image
docker build -t abc-telecom-billing:1.0 .

# Tag for registry
docker tag abc-telecom-billing:1.0 myregistry/abc-telecom-billing:1.0

# Push to registry
docker push myregistry/abc-telecom-billing:1.0

# List images
docker images | grep abc-telecom

# Remove image
docker rmi abc-telecom-billing:1.0

# View container logs
docker logs abc-telecom-app -f

# Execute command in container
docker exec -it abc-telecom-app /bin/bash

# Container stats
docker stats abc-telecom-app
```

## Kubernetes Deployment

### Prerequisites
- Kubernetes cluster (v1.19+)
- kubectl configured
- Docker registry access (if using private registry)

### Deploy to Kubernetes

1. **Apply manifests**
   ```bash
   kubectl apply -f kubernetes-manifest.yaml
   ```

2. **Verify deployment**
   ```bash
   # Check namespace
   kubectl get namespace abc-telecom
   
   # Check pods
   kubectl get pods -n abc-telecom
   
   # Check services
   kubectl get svc -n abc-telecom
   
   # Check statefulsets
   kubectl get statefulsets -n abc-telecom
   ```

3. **Access the application**
   ```bash
   # Get LoadBalancer IP/DNS
   kubectl get service abc-telecom-service -n abc-telecom
   
   # Port-forward for local access
   kubectl port-forward svc/abc-telecom-service 8080:80 -n abc-telecom
   
   # API will be available at http://localhost:8080
   ```

### Kubernetes Management Commands

```bash
# View logs
kubectl logs -f deployment/abc-telecom-app -n abc-telecom
kubectl logs -f statefulset/postgres -n abc-telecom

# Describe resources
kubectl describe pod <pod-name> -n abc-telecom
kubectl describe deployment abc-telecom-app -n abc-telecom

# Scale deployment
kubectl scale deployment abc-telecom-app --replicas=3 -n abc-telecom

# Update image
kubectl set image deployment/abc-telecom-app \
  abc-telecom-app=myregistry/abc-telecom-billing:2.0 \
  -n abc-telecom

# View HPA status
kubectl get hpa -n abc-telecom

# Delete deployment
kubectl delete -f kubernetes-manifest.yaml
```

### Multi-environment Setup

**Create separate namespace for each environment:**

```bash
# Development
kubectl create namespace abc-telecom-dev
kubectl apply -f kubernetes-manifest.yaml -n abc-telecom-dev

# Staging
kubectl create namespace abc-telecom-staging
kubectl apply -f kubernetes-manifest.yaml -n abc-telecom-staging

# Production
kubectl create namespace abc-telecom-prod
kubectl apply -f kubernetes-manifest.yaml -n abc-telecom-prod
```

## Production Considerations

### Security

1. **Secrets Management**
   - Use HashiCorp Vault or AWS Secrets Manager
   - Never commit secrets to git
   - Rotate secrets regularly

2. **TLS/SSL**
   - Enable HTTPS in production
   - Use valid SSL certificates
   - Configure in Spring Boot:
     ```properties
     server.ssl.key-store=classpath:keystore.jks
     server.ssl.key-store-password=password
     server.ssl.key-store-type=JKS
     ```

3. **Network Policies**
   ```yaml
   apiVersion: networking.k8s.io/v1
   kind: NetworkPolicy
   metadata:
     name: abc-telecom-network-policy
     namespace: abc-telecom
   spec:
     podSelector:
       matchLabels:
         app: abc-telecom-app
     policyTypes:
     - Ingress
     - Egress
     ingress:
     - from:
       - podSelector:
           matchLabels:
             app: nginx-ingress
     egress:
     - to:
       - podSelector:
           matchLabels:
             app: postgres
   ```

### Database

1. **Backup Strategy**
   ```bash
   # Automated daily backups
   kubectl exec -it postgres-0 -n abc-telecom -- \
     pg_dump -U postgres abc_telecom_db > backup-$(date +%Y%m%d).sql
   ```

2. **Connection Pooling**
   - Configured in application-prod.properties
   - Adjust HikariCP settings based on load

3. **Replication (Optional)**
   - Set up PostgreSQL streaming replication
   - Use PostgreSQL HA solutions like Patroni

### Monitoring

1. **Prometheus Metrics**
   ```bash
   # Enable metrics in Spring Boot
   management.endpoints.web.exposure.include=health,metrics,prometheus
   ```

2. **Logging Aggregation**
   - Use ELK Stack (Elasticsearch, Logstash, Kibana)
   - Or use cloud-native solutions (CloudWatch, Stackdriver)

### Auto-scaling

Already configured in kubernetes-manifest.yaml:
- Minimum 2 replicas
- Maximum 5 replicas
- Target CPU: 70%
- Target Memory: 80%

## Monitoring and Logging

### Health Checks

```bash
# Application health
curl http://localhost:8080/actuator/health

# Database connectivity
curl http://localhost:8080/api/login
```

### Logs Collection

**Docker**
```bash
docker logs abc-telecom-app > app.log
docker logs postgres > postgres.log
```

**Kubernetes**
```bash
# Application logs
kubectl logs -f deployment/abc-telecom-app -n abc-telecom

# PostgreSQL logs
kubectl logs -f statefulset/postgres -n abc-telecom

# All logs in namespace
kubectl logs -n abc-telecom -l app
```

### Metrics

**Prometheus Endpoints** (if configured):
- `/actuator/metrics`
- `/actuator/metrics/jvm.memory.used`
- `/actuator/metrics/http.server.requests`

### Troubleshooting

**Common Issues:**

1. **Pod not starting**
   ```bash
   kubectl describe pod <pod-name> -n abc-telecom
   kubectl logs <pod-name> -n abc-telecom
   ```

2. **Database connection issues**
   ```bash
   # Test connection
   kubectl exec -it abc-telecom-app-xxxxx -n abc-telecom \
     -- curl jdbc:postgresql://postgres-service:5432/abc_telecom_db
   ```

3. **Memory issues**
   ```bash
   # Check resource usage
   kubectl top pod -n abc-telecom
   kubectl top node
   ```

## Rollback Procedure

```bash
# View rollout history
kubectl rollout history deployment/abc-telecom-app -n abc-telecom

# Rollback to previous version
kubectl rollout undo deployment/abc-telecom-app -n abc-telecom

# Rollback to specific revision
kubectl rollout undo deployment/abc-telecom-app -n abc-telecom --to-revision=2
```

## Performance Tuning

### JVM Tuning
```properties
# In Dockerfile or deployment env vars
JAVA_OPTS=-Xmx512m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200
```

### Database Query Optimization
- Add indexes on frequently queried columns
- Use connection pooling
- Implement caching (Redis)

### API Response Optimization
- Enable gzip compression
- Use pagination for large datasets
- Implement caching headers

## Disaster Recovery

1. **Database Backups**
   - Automated daily backups to S3/Cloud Storage
   - Test restore procedures regularly

2. **Configuration Backup**
   - Store ConfigMaps and Secrets in version control (encrypted)

3. **RTO/RPO Targets**
   - Recovery Time Objective (RTO): 1 hour
   - Recovery Point Objective (RPO): 24 hours

## Cost Optimization

1. **Resource Requests**
   - Set appropriate resource requests and limits
   - Use HPA for automatic scaling

2. **Reserved Capacity**
   - Use reserved instances for stable baseline

3. **Cluster Autoscaling**
   ```bash
   # Enable cluster autoscaling
   gcloud container clusters update my-cluster \
     --enable-autoscaling \
     --min-nodes 1 \
     --max-nodes 10
   ```
