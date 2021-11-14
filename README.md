![logo.jpg](logo.jpg)
- Spring Boot
- Spring Data JPA
- Jackson
- Lombok
- H2 Database
- JUnit 5
- Swagger/ OpenAPI 3.0
-----------------------------------------------------
The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed
* Each restaurant provides a new menu each day.
-----------------------------------------------------
- Run: `mvn spring-boot:run` in root directory.
-----------------------------------------------------
[REST API documentation](http://localhost:8080/swagger-ui.html)  
Креденшелы:
```
User:  user@yandex.ru / password
Admin: admin@gmail.com / admin
```
## Description:
Authentication done based on login (email) and password. Authorization done based on roles.
We use HTTP Basic authentication. Login and password are transmitted in the request header in open form, Base64 is used.
With basic authorization, the database is contacted with each request. Therefore, we use Caching of the query result in DB during authentication.

Regular users can view the menu of all restaurants for today.
As a result, they make a decision and vote.
Users can view the history of all their votes / between dates / for a specific restaurant.
Admin manage the restaurants / menus / dishes.

To display a list of restaurants without a menu and a list of votes, we use the DTO.

We got the following controllers for controlling the application:
* profile-controller
* admin-user-controller
* admin-dish-controller
* admin-menu-controller
* admin-restaurant-controller
* restaurant-controller
* profile-vote-controller
* admin-vote-controller