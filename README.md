# URL Shortener

A lightweight URL shortening service built with Java 17 and Spring Boot. Supports configurable link expiry, Base62 short codes, scheduled cleanup, and in-memory caching.

## Why this project?

URL shorteners are deceptively simple on the surface but hide real engineering decisions underneath — short code generation without race conditions, redirect semantics, expiry cleanup, and cache invalidation. I built this to understand how to design a high-read, low-write system and to get hands-on with Spring Boot beyond basic CRUD.

## Core Features

- **Shorten any URL** — generates a unique 6-character Base62 short code
- **Fast redirect** — `GET /{shortCode}` resolves and redirects with HTTP 302
- **Configurable link expiry** — set a custom TTL per link or fall back to a system default; expired links return `410 Gone`
- **Scheduled cleanup** — a daily cron job purges expired records from the database at midnight
- **In-memory cache** — active short codes are cached; the cache is evicted on every cleanup run
- **Input validation** — rejects malformed URLs, non-HTTP/S schemes, and missing hostnames before they reach the database
- **Structured exception handling** — consistent error responses across all failure cases
- **SLF4J logging** — key operations (creation, redirect, cleanup) are logged for observability

## Tech Stack

| | Technology |
|---|---|
| **Language** | Java 17 |
| **Framework** | Spring Boot |
| **Database** | PostgreSQL |
| **ORM** | Spring Data JPA |
| **Caching** | Spring Cache (in-memory) |
| **Logging** | SLF4J |

- (Planned) Redis, Rate Limiting, Analytics


## API Overview

### POST /shorten : 
Creates a short URL.

Request:
{
  "longUrl": "https://example.com",
  "expiryDays": 7
}

Response:
{
  "shortUrl": "http://localhost:8080/aB12xZ",
  "expiryDate": "2026-02-10T17:30:00"
}

### GET /{shortCode} : 
Redirects to the original URL.

| Scenario | Response |
|---|---|
| Valid, active link | `302 Found` |
| Code not found | `404 Not Found` |
| Link expired | `410 Gone` |

---

## Engineering Decisions
 
### Sequence-based ID for short code generation — no double DB write
To generate a unique short code, the service fetches the next value from a PostgreSQL sequence, then Base62-encodes it:
 
```java
@Query(value = "SELECT nextval('url_sequence')", nativeQuery = true)
Long getNextSequenceValue();
```
 
This solves a subtle problem: save the record first to get a generated ID, then update it with the derived short code — two writes per request. The sequence lets us compute the short code *before* the insert, so the record is saved once with the short code already set. No race conditions, no double writes.
 
Base62 (`[a-z A-Z 0-9]`) encodes the sequence ID into a compact, URL-safe string. A 6-character code covers 62⁶ ≈ 56 billion combinations.
 
### Why HTTP 302 instead of 301?
302 is a temporary redirect — the browser does not cache it, so every visit makes a server round-trip. 301 is permanent and browsers cache it aggressively. 302 keeps redirect control on the server.
 
### URL validation — scheme and host checks
Rather than a regex, validation uses Java's `URI` parser directly. This rejects `javascript:`, `ftp://`, mailto links, and anything without a valid host.
 
### Configurable expiry with a system default
Each link stores an `expiresAt` timestamp. Users can pass `expiresInDays` in the request; omitting it falls back to a configured default. On every redirect, `expiresAt` is checked against `LocalDateTime.now()`. Expired links return `410 Gone` rather than `404`.
 
### Scheduled cleanup with cache eviction
Expired records are purged daily by a cron job:
 
```java
@Scheduled(cron = "0 0 0 * * *")
@CacheEvict(value = "urls", allEntries = true)
@Transactional
public void purgeExpiredLinks() {
    int deletedCount = urlRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    LOG.info("Deleted {} expired records and cleared cache.", deletedCount);
}
```
