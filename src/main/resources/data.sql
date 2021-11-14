INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO USER_ROLES (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (NAME)
VALUES ('KFC'),
       ('McDonalds'),
       ('BurgerKing');

INSERT INTO MENU (DATE, RESTAURANT_ID)
VALUES (CURRENT_DATE - 1, 1),
       (CURRENT_DATE - 1, 2),
       (CURRENT_DATE - 1, 3),
       (CURRENT_DATE, 1),
       (CURRENT_DATE, 2),
       (CURRENT_DATE, 3);

INSERT INTO DISH (name, price, menu_id)
VALUES ('Chicken wings', 120, 1),
       ('Burger', 120, 1),
       ('Coke', 90, 1),
       ('Sauce', 30, 1),
       ('Cheeseburger', 120, 2),
       ('Mustard', 30, 2),
       ('Chips', 90, 2),
       ('Soda', 70, 2),
       ('Buffalo wings', 120, 3),
       ('French fries', 130, 3),
       ('Mayo', 30, 3),
       ('Ice cream', 80, 3),
       ('Hamburger', 130, 4),
       ('Hot-dog', 110, 4),
       ('Mayonnaise', 30, 4),
       ('Milkshake', 80, 4),
       ('Nachos with cheese', 110, 5),
       ('Onion rings', 80, 5),
       ('Pizza', 100, 5),
       ('Popcorn', 50, 5),
       ('Fish and chips', 120, 6),
       ('Sandwich', 110, 6),
       ('Sauce', 50, 6),
       ('Wrap', 120, 6);

INSERT INTO VOTE (DATE, RESTAURANT_ID, USER_ID)
VALUES (CURRENT_DATE - 1, 1, 1),
       (CURRENT_DATE, 3, 1),
       (CURRENT_DATE - 1, 2, 2);