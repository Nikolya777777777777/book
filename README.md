## Configuration

1. Copy file `.env.template` to `.env`: cp .env.template .env
2. Fill in the values in `.env`.
3. Start application: docker-compose up --build

I'm interested in reading books and so I decided to create a book store as a Spring project. Now it is working shop, but without front-end where 
admins can create new categories and add books, users can make an order and can save books they want to order in shopping cart and a lot of other usefully features.

## Features I used in my project 
- **Category Management** – CRUD operations for categories with soft delete support.
- **Book Management** – CRUD operations for books, with category assignments.
- **User Roles** – Basic authentication and role-based access (`USER`, `ADMIN`).
- **Bearer Token Authentication** – All protected endpoints require a valid JWT token.
- **Pagination & Sorting** – For listing large datasets efficiently.
- **API Documentation** – Interactive Swagger UI.
- **Database Migrations** – Managed with Liquibase.
- **Integration & Unit Tests** – Ensuring stability and correctness.

## Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security**
- **Hibernate**
- **Swagger/OpenAPI**
- **JUnit 5 & Mockito**
- **Maven**
- **MySQL**

## How you can set up and run my project
1) Clone the repository

git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name

2) Configure the database 
   Update src/main/resources/application.properties with your database credentials:

spring.datasource.url=jdbc:mysql://localhost:3306/bookstore
spring.datasource.username=yourusername
spring.datasource.password=yourpassword

3) Build your project

mvn clean package

4) Build and run the application

mvn spring-boot:run

5) Access the API

Swagger UI: http://localhost:8080/swagger-ui/index.html
Secured endpoints require a valid Bearer Token in the Authorization header:
Authorization: Bearer <your_token>

## Changes I faced during this project and how I overcame them
1) Database migration conflicts – Initially, some Mysql changesets caused inconsistencies between environments. 
This was solved by splitting migrations into smaller, atomic files and enforcing version control for changelogs.
And also I had problems with foreign and primary keys.

2) JWT authentication issues – Early token validation bugs were fixed by implementing a custom JWT filter and adding comprehensive integration tests.

3) Pagination and filtering – Complex queries for retrieving books by multiple categories were optimized using Spring Data JPA’s Specification API.

4) Testing - I had problems with dropping and creating tables in database, but I solved this with excluding keys and truncating all tables.

Collection of Postman requests
In each request you need to use Authentication through Bearer token, so before each request login into system 
and remember that this token is valid for 5 minutes then you will need to re-login in order to get new token 
and remember that if you send request to add, delete, update your user need to have role Admin 
1.Book
1) GET request - http://localhost:8082/api/books/1 - you will get book by id(in url id = 1)
2) POST request - http://localhost:8082/api/books - you need also send body with params in json and then you will get book which was saved to database
3) GET request - http://localhost:8082/api/books - you will get all existing books by
4) DELETE request - http://localhost:8082/api/books/2 - you will delete book by id(in url id = 2)
5) PUT request - http://localhost:8082/api/books/1 - you need also send body with params in json and then you will get book with changed params
6) GET request - http://localhost:8080/books/search?authors=Anton&size=5&sort=price - you will get all books by params which match params that you gave in url 
7) GET request - http://localhost:8082/api/books?page=0&size=5&sort=price,desc - you will get all books by given params about page in url
8) GET request - http://localhost:8082/api/categories/1/books - you will get All books which have the same category id as given in url(in url id = 1)
2.Category
1) POST request - http://localhost:8082/api/categories - you need also send body with params in json and then you will get category which was saved to database
2) GET request - http://localhost:8082/api/categories/1 - you will get category by id(in url id = 1)
3) GET request - http://localhost:8082/api/categories/1/books - you will get all books by category id(in url category id = 1)
3.Order
1) POST request - http://localhost:8082/api/orders - you need also send body with params in json and then you will get order which was saved to database 
2) GET request - http://localhost:8082/api/orders - you will get all Users orders
3) PATCH request - http://localhost:8082/api/orders/16 - you need to send body with params in json and then you will get updated category which was updated in database
4) GET request - http://localhost:8082/api/orders/16/items - you will get all items in Order by order id (in url order id = 16)
5) GET request - http://localhost:8082/api/orders/16/items/7 - you will get all order items in order by order id and item id (in url order id = 16, item id = 7)
4.ShoppingCart
1) GET request - http://localhost:8082/api/cart - you will get shopping cart for user
2) POST request - http://localhost:8082/api/cart - you need to send body with params in json and then you will get shopping cart which was saved to database
3) PUT request - http://localhost:8082/api/cart/items/2 - you need to send body with params in json and then you will get updated shopping cart which was updated in database
4) DELETE request - http://localhost:8082/api/cart/items/2 - you will delete cart item in shopping cart by id(in url id = 2)
5.User
1) POST request - http://localhost:8082/api/auth/registration - you need to send body with params in json and then you will get user which was saved to database
2) POST request - http://localhost:8083/api/auth/login - you need to send credentials for login(username and password) and then you will get jwt token which you need to send all requests

```mermaid
flowchart TD
    A[Client] -->|POST /auth/login| B[Auth Controller]
    B -->|Verify credentials| C[User Service]
    C -->|Generate JWT| D[Return Bearer Token]
    A -->|Request with Bearer Token| E[Protected Endpoint]
    E -->|JWT Validation| F[Security Filter]
    F -->|Authorized| G[Controller Method]
    G -->|Business Logic| H[Service Layer]
    H -->|Data Access| I[Repository / DB]
    I -->|Response| A