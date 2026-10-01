# HealthBridge

A backend REST API for a Hospital Management System, built with Java and Spring Boot. HealthBridge manages hospitals, doctors, patients, and appointments through a layered, validated, and documented API.

## Overview

HealthBridge exposes CRUD operations across four core entities — **Hospital**, **Doctor**, **Patient**, and **Appointment** — with request validation, centralized exception handling, and interactive API documentation via Swagger/OpenAPI.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web (MVC)
- Spring Data JPA / Hibernate
- MySQL
- Maven
- Lombok
- springdoc-openapi (Swagger UI)

## Project Structure

```
com.healthbridge
├── controller      REST endpoints
├── dto             Request payloads (create/update) with validation
├── model            JPA entities
├── repo             Spring Data JPA repositories
├── service          Service interfaces
├── serviceimpl      Service implementations
├── exception        Global exception handling
└── HealthBridgeApplication.java
```

## Features

- Hospital management (create, update, delete, search by name/location)
- Doctor management (create, update, delete, search by hospital, specialization, or name)
- Patient management (create, update, delete, search by name or phone)
- Appointment management (create, update, delete, filter by doctor, patient, date, or status)
- Request validation on all create/update operations (`jakarta.validation`)
- Centralized exception handling with a custom `ResourceNotFoundException`
- Interactive API docs via Swagger UI

## Getting Started

### Prerequisites

- Java 21+
- Maven (or use the included `mvnw` wrapper)
- MySQL running locally

### Configuration

Database connection is read from environment variables, with sensible local defaults:

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/healthbridge_db` | JDBC connection URL |
| `DB_USERNAME` | `root` | MySQL username |
| `DB_PASSWORD` | *(empty)* | MySQL password |

Create the database before running the app:

```sql
CREATE DATABASE healthbridge_db;
```

Set your credentials (example, Linux/macOS):

```bash
export DB_URL=jdbc:mysql://localhost:3306/healthbridge_db
export DB_USERNAME=root
export DB_PASSWORD=your_password
```

### Run the application

```bash
./mvnw spring-boot:run
```

The API will start on `http://localhost:8080`.

### API Documentation

Once running, Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

## API Endpoints

### Hospital — `/hospital/api/v1`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/insertHs` | Create a hospital |
| GET | `/getHospitals` | List all hospitals |
| GET | `/getHospitalById/{id}` | Get hospital by ID |
| GET | `/getHospitalByName/{name}` | Search hospitals by name |
| GET | `/getHospitalByLocation/{location}` | Search hospitals by location |
| PUT | `/updateById/{id}` | Update a hospital |
| DELETE | `/deleteById/{id}` | Delete a hospital |

### Doctor — `/doctor/api/v1`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/insertDoc` | Create a doctor |
| GET | `/getDoctors` | List all doctors |
| GET | `/getDoctorById/{id}` | Get doctor by ID |
| GET | `/getDoctorsByHId/{HId}` | Get doctors by hospital ID |
| GET | `/getDoctorBySpecialization/{specialization}` | Search doctors by specialization |
| GET | `/getDoctorByName/{name}` | Search doctors by name |
| PUT | `/updateById/{id}` | Update a doctor |
| DELETE | `/deleteById/{id}` | Delete a doctor |

### Patient — `/patient/api/v1`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/insertPa` | Create a patient |
| GET | `/getPatientById/{id}` | Get patient by ID |
| GET | `/getByName/{name}` | Search patients by name |
| GET | `/getByPhone/{phone}` | Get patient by phone number |
| PUT | `/updateById/{id}` | Update a patient |
| DELETE | `/deleteById/{id}` | Delete a patient |

### Appointment — `/appointment/api/v1`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/insertAp` | Create an appointment |
| GET | `/getAllAppointments` | List all appointments |
| GET | `/getById/{id}` | Get appointment by ID |
| GET | `/byDoctor/{doctorId}` | Get appointments by doctor |
| GET | `/byPatient/{patientId}` | Get appointments by patient |
| GET | `/byDate/{date}` | Get appointments by date |
| GET | `/byStatus/{status}` | Get appointments by status |
| PUT | `/updateById/{id}` | Update an appointment |
| DELETE | `/deleteById/{id}` | Delete an appointment |

## Web Interface

A Bootstrap 5 single-page front end is served by Spring Boot itself. After `./mvnw spring-boot:run`, open `http://localhost:8080/`.

- Dashboard with today's schedule, upcoming visits and status breakdown
- Search, add, edit and delete for hospitals, doctors, patients and appointments
- Field-level validation messages from the API, dark mode, responsive layout
- New endpoint: `GET /patient/api/v1/getPatients` (list all patients)
- Appointment responses now include the doctor and patient, and doctor responses include the hospital

## Future Enhancements

- JWT-based authentication and Spring Security
- Pagination and sorting on list endpoints
- Dockerized deployment
- Unit and integration test coverage

## License

This project is available for portfolio and educational use.
