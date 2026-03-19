# URL Shortener (Java)

A simple URL shortening service built using Java.  
The application generates a short URL for a long URL and redirects users to the original link.

## Features
- Generate short URLs
- Redirect short URLs to original URLs
- Store URL mappings in MySQL
- Track number of clicks
- In-memory caching using HashMap

## Technologies Used
- Java
- HTTP Server (com.sun.net.httpserver)
- JDBC
- MySQL
- Base62 encoding for short URL generation

## Project Structure

src/com/urlshortner
- Main.java
- repository/UrlRepository.java
- util/Base62Encoder.java
- util/DatabaseConnection.java
- util/UrlCache.java

## How to Run

1. Install MySQL and create database:

CREATE DATABASE url_shortener;

2. Create table:

CREATE TABLE urls (
id INT AUTO_INCREMENT PRIMARY KEY,
short_code VARCHAR(20) UNIQUE,
long_url TEXT,
click_count INT DEFAULT 0
);

3. Compile the project:

javac -cp ".;lib\mysql-connector-j-9.6.0.jar" -d . src\com\urlshortner\*.java

4. Run the server:

java -cp ".;lib\mysql-connector-j-9.6.0.jar" com.urlshortner.Main

## Example

Create short URL:

http://localhost:8080/shorten?url=https://google.com

Example result:

http://localhost:8080/b

Opening the short URL redirects to the original link.
