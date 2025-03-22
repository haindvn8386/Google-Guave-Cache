![image](https://github.com/user-attachments/assets/467f4bc8-96f1-4311-aec2-d155866e306c)

Local Caching Implementation with Spring Boot

This project demonstrates a local caching solution using Spring Boot (Java) to store a list of airports (Code, Name) with a configurable Time-To-Live (TTL). It leverages Google’s Guava library for caching, implements the read-aside caching pattern, and uses Lombok to reduce boilerplate code. The application includes logging for cache-related events, such as key errors and cache misses, to facilitate debugging and monitoring.

Features:

Local cache for airport data with TTL expiration.
Integration with Guava cache library.
Read-aside caching strategy.
Lombok for cleaner code (e.g., getters, setters, constructors).
Logging of cache key errors and misses using SLF4J.
Requirements:

Java 17+
Spring Boot 3.x
Dependencies: Guava, Lombok, SLF4J
