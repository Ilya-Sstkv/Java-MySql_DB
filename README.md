Console application that simulates the work of the simplest social network. 
Information about all users, including their ids, names, passwords etc. is stored in the database integrated into the program.

Authorization as an admin allows you to add/remove users of the social network, as well as send messageы to all online users (authorized in parallel-running main files) through the server-socket system.
Authorization as a user allows you to view other users' profiles, add/remove users as "friends", and receive messages sent by the admin.
All interactions with the database through the authorized user menu can be observed in real time.
Sending a message through the server-socket system does not affect the information in the database.

The application is written on Java and uses a MySql database. Interface language - Russian.


//Code for DB 'user_pages' creation:
  CREATE TABLE `user_pages` (
    `id` int NOT NULL,
    `name` varchar(45) NOT NULL,
    `password` varchar(45) NOT NULL,
    `friend` int NOT NULL,
    `online` tinyint NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3
