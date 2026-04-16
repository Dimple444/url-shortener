# URL Shortener

A simple, production‑style URL Shortener built using Java & Spring Boot to demonstrate backend fundamentals and system design concepts.

## Why this project?
This project focuses on designing a read‑heavy, scalable backend service, not just CRUD APIs.
It demonstrates understanding of:

- REST API design
- Database modeling
- ID generation (Base62)
- Read‑path optimization
- Clean layered architecture
- Scalability considerations


## Core Features

- Generate short URLs from long URLs
- Redirect short URLs to original URLs
- URL expiration support
- Clean REST API design
- Layered architecture (Controller → Service → Repository)


## Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- (Planned) Redis, Rate Limiting, Analytics


## API Overview

### POST /shorten : 
Creates a short URL.

Request:
{
  "longUrl": "https:example.com",
  "expiryDays": 7
}

Response:
{
  "shortUrl": "http://localhost:8080/aB12xZ",
  "expiryDate": "2026-02-10T17:30:00"
}

### GET /{shortCode} : 
Redirects to the original URL.
