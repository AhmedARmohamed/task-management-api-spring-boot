# Task Management API

A RESTful API service for a simple task management system built with Spring Boot and Java 21.

## Features

- **User Management**: Create, retrieve, update, delete users with authentication
- **Task Management**: Complete CRUD operations for tasks with user ownership
- **JWT Authentication**: Secure API endpoints with JWT tokens
- **PostgreSQL Database**: Persistent data storage
- **API Documentation**: Swagger/OpenAPI documentation
- **Docker Support**: Easy deployment with Docker Compose

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- Docker & Docker Compose
- PostgreSQL (if running without Docker)

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/AhmedARmohamed/task-management-api-spring-boot
cd task-management-api
```

### 2. Start PostgreSQL Database

```bash
docker-compose up -d
```

This will start:
- PostgreSQL database on port 5432
- pgAdmin on port 5050 (admin/admin)

### 3. Build and Run the Application

```bash
mvn clean package
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Access API Documentation

Visit `http://localhost:8080/swagger-ui/index.html` to view the interactive API documentation.

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Register a new user |
| POST | `/api/v1/auth/authenticate` | Login user |
| POST | `/api/v1/auth/refresh-token` | Refresh JWT token |
| POST | `/api/v1/auth/logout` | Logout user |

### Users

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/users` | Get all users | ✅ |
| GET | `/api/v1/users/{id}` | Get user by ID | ✅ |
| GET | `/api/v1/users/search?name={name}` | Search users by name | ✅ |
| PUT | `/api/v1/users/{id}` | Update user (own profile only) | ✅ |
| DELETE | `/api/v1/users/{id}` | Delete user (own profile only) | ✅ |
| PATCH | `/api/v1/users/change-password` | Change password | ✅ |

### Tasks

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/v1/tasks` | Get all tasks | ✅ |
| GET | `/api/v1/tasks/{id}` | Get task by ID | ✅ |
| GET | `/api/v1/tasks/user/{userId}` | Get tasks by user ID | ✅ |
| POST | `/api/v1/tasks` | Create new task | ✅ |
| PUT | `/api/v1/tasks/{id}` | Update task (owner only) | ✅ |
| DELETE | `/api/v1/tasks/{id}` | Delete task (owner only) | ✅ |

## Usage Examples

### 1. Register a New User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "password": "securePassword123",
    "dateOfBirth": "1990-01-15"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "securePassword123"
  }'
```

### 3. Create a Task

```bash
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Complete project documentation",
    "description": "Write comprehensive documentation for the task management API",
    "dueDate": "2024-12-31T23:59:59",
    "taskStatus": "TODO"
  }'
```

### 4. Get All Tasks

```bash
curl -X GET http://localhost:8080/api/v1/tasks \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 5. Search Users

```bash
curl -X GET "http://localhost:8080/api/v1/users/search?name=John" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Data Models

### User Entity

```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "dateOfBirth": "1990-01-15"
}
```

### Task Entity

```json
{
  "id": 1,
  "title": "Complete project documentation",
  "description": "Write comprehensive documentation for the task management API",
  "dueDate": "2024-12-31T23:59:59",
  "taskStatus": "TODO",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00",
  "userId": 1
}
```

### Task Status Options

- `TODO`
- `IN_PROGRESS`
- `DONE`

## Security

- All endpoints (except authentication) require JWT token
- Users can only update/delete their own profile
- Users can only update/delete their own tasks
- Passwords are encrypted using BCrypt
- JWT tokens expire after 24 hours
- Refresh tokens expire after 7 days

## Database Configuration

The application uses PostgreSQL by default. Configuration can be found in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/taskmanagement
    username: username
    password: password
```

## Development

### Running Tests

```bash
mvn test
```

### Building for Production

```bash
mvn clean package -DskipTests
java -jar target/task-management-api-0.0.1-SNAPSHOT.jar
```

## Default Users

The application creates two default users on startup:

1. **Admin User**
    - Email: admin@mail.com
    - Password: password

2. **Manager User**
    - Email: manager@mail.com
    - Password: password

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.
