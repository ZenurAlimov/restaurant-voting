INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO USER_ROLES (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (name)
VALUES ('KFC'),
       ('McDonalds'),
       ('BurgerKing');

INSERT INTO DISH (name, price, date, restaurant_id)
VALUES ('Chicken wings', 120, CURRENT_DATE - 1, 1),
       ('Burger', 120, CURRENT_DATE - 1, 1),
       ('Coke', 90, CURRENT_DATE - 1, 1),
       ('Sauce', 30, CURRENT_DATE - 1, 1),
       ('Cheeseburger', 120, CURRENT_DATE - 1, 2),
       ('Mustard', 30, CURRENT_DATE - 1, 2),
       ('Chips', 90, CURRENT_DATE - 1, 2),
       ('Soda', 70, CURRENT_DATE - 1, 2),
       ('Buffalo wings', 120, CURRENT_DATE - 1, 3),
       ('French fries', 130, CURRENT_DATE - 1, 3),
       ('Mayo', 30, CURRENT_DATE - 1, 3),
       ('Ice cream', 80, CURRENT_DATE - 1, 3),
       ('Hamburger', 130, CURRENT_DATE, 1),
       ('Hot-dog', 110, CURRENT_DATE, 1),
       ('Mayonnaise', 30, CURRENT_DATE, 1),
       ('Milkshake', 80, CURRENT_DATE, 1),
       ('Nachos with cheese', 110, CURRENT_DATE, 2),
       ('Onion rings', 80, CURRENT_DATE, 2),
       ('Pizza', 100, CURRENT_DATE, 2),
       ('Popcorn', 50, CURRENT_DATE, 2),
       ('Fish and chips', 120, CURRENT_DATE, 3),
       ('Sandwich', 110, CURRENT_DATE, 3),
       ('Sauce', 50, CURRENT_DATE, 3),
       ('Wrap', 120, CURRENT_DATE, 3);

INSERT INTO VOTE (date, restaurant_id, user_id)
VALUES (CURRENT_DATE - 1, 1, 1),
       (CURRENT_DATE, 3, 1),
       (CURRENT_DATE - 1, 2, 2);