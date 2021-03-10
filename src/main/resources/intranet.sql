-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: localhost    Database: intranet
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary view structure for view `auth`
--

DROP TABLE IF EXISTS `auth`;
/*!50001 DROP VIEW IF EXISTS `auth`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `auth` AS SELECT 
 1 AS `username`,
 1 AS `password`,
 1 AS `enabled`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `authorities`
--

DROP TABLE IF EXISTS `authorities`;
/*!50001 DROP VIEW IF EXISTS `authorities`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `authorities` AS SELECT 
 1 AS `id_user`,
 1 AS `username`,
 1 AS `authority`,
 1 AS `descrizione`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `authorities_from_permissions`
--

DROP TABLE IF EXISTS `authorities_from_permissions`;
/*!50001 DROP VIEW IF EXISTS `authorities_from_permissions`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `authorities_from_permissions` AS SELECT 
 1 AS `id_user`,
 1 AS `username`,
 1 AS `authority`,
 1 AS `descrizione`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `authorities_from_roles`
--

DROP TABLE IF EXISTS `authorities_from_roles`;
/*!50001 DROP VIEW IF EXISTS `authorities_from_roles`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `authorities_from_roles` AS SELECT 
 1 AS `id_user`,
 1 AS `username`,
 1 AS `authority`,
 1 AS `descrizione`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (3);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `nome` varchar(255) NOT NULL,
  `descrizione` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
INSERT INTO `permissions` VALUES ('AM','Creazione nuovo profilo, eliminazione, modifica dati e assegnazione permessi'),('CPS','Aggiunta dei commenti ai post ed eliminazione (SOLO dei propri commenti)'),('DCS','Eliminazione commenti altrui'),('DPS','Cancellazione dei post altrui'),('EP','Modifica il proprio profilo'),('PM','Creazione nuovo ruolo, eliminazione, assegnazione permessi al ruolo e visualizzazione dei permessi assegnati'),('RP','Visualizza il proprio profilo'),('RS','Lettura social (post in myLife)'),('WNML','Aggiunta news in myLife, modifica ed eliminazione'),('WNMW','Aggiunta news in myWork, modifica ed eliminazione'),('WPS','Aggiungere post su myLife, modifica ed eliminazione (SOLO dei propri post)');
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `nome` varchar(255) NOT NULL,
  PRIMARY KEY (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES ('ADMIN'),('USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles_permissions`
--

DROP TABLE IF EXISTS `roles_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles_permissions` (
  `id_permission` varchar(255) NOT NULL,
  `id_role` varchar(255) NOT NULL,
  PRIMARY KEY (`id_permission`,`id_role`),
  KEY `FKiodo5208nfgl9tgi0evftj9ld` (`id_role`),
  CONSTRAINT `FKiodo5208nfgl9tgi0evftj9ld` FOREIGN KEY (`id_role`) REFERENCES `roles` (`nome`),
  CONSTRAINT `FKma4k0j1pgmtwls8s4tw0him5` FOREIGN KEY (`id_permission`) REFERENCES `permissions` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles_permissions`
--

LOCK TABLES `roles_permissions` WRITE;
/*!40000 ALTER TABLE `roles_permissions` DISABLE KEYS */;
INSERT INTO `roles_permissions` VALUES ('AM','ADMIN'),('CPS','ADMIN'),('DCS','ADMIN'),('DPS','ADMIN'),('EP','ADMIN'),('PM','ADMIN'),('RP','ADMIN'),('RS','ADMIN'),('WNML','ADMIN'),('WNMW','ADMIN'),('WPS','ADMIN'),('CPS','USER'),('EP','USER'),('RP','USER'),('RS','USER'),('WPS','USER');
/*!40000 ALTER TABLE `roles_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL,
  `attivo` bit(1) DEFAULT NULL,
  `cognome` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,_binary '','Agostini','marco.agostini@errepro.tech','Marco','$2a$10$x4OLz12.Cw0PAvoWhS/35eJpUEd5v8612u/2SiddDkEO7nPt17wMm'),(2,_binary '','Paoletti','mario.paoletti@errepro.tech','Mario','$2a$10$cjZVepk8dtlHvLJgGT5lbefa1XbLGWtd56ttQN/GghC86gS/bk/FW');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_permissions`
--

DROP TABLE IF EXISTS `users_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_permissions` (
  `id_permission` varchar(255) NOT NULL,
  `id_user` int NOT NULL,
  PRIMARY KEY (`id_permission`,`id_user`),
  KEY `FKshuqu2ibei5os4f4auamm3nlh` (`id_user`),
  CONSTRAINT `FKky8m070sfv5vpjpexm1vysgp6` FOREIGN KEY (`id_permission`) REFERENCES `permissions` (`nome`),
  CONSTRAINT `FKshuqu2ibei5os4f4auamm3nlh` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_permissions`
--

LOCK TABLES `users_permissions` WRITE;
/*!40000 ALTER TABLE `users_permissions` DISABLE KEYS */;
INSERT INTO `users_permissions` VALUES ('WNML',2);
/*!40000 ALTER TABLE `users_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_roles` (
  `id_role` varchar(255) NOT NULL,
  `id_user` int NOT NULL,
  PRIMARY KEY (`id_role`,`id_user`),
  KEY `FK6ywr92flw5416dup8uc2egb83` (`id_user`),
  CONSTRAINT `FK3avenccqsoqwrfur1hb8mpbrw` FOREIGN KEY (`id_role`) REFERENCES `roles` (`nome`),
  CONSTRAINT `FK6ywr92flw5416dup8uc2egb83` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_roles`
--

LOCK TABLES `users_roles` WRITE;
/*!40000 ALTER TABLE `users_roles` DISABLE KEYS */;
INSERT INTO `users_roles` VALUES ('ADMIN',1),('USER',1),('USER',2);
/*!40000 ALTER TABLE `users_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `auth`
--

/*!50001 DROP VIEW IF EXISTS `auth`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `auth` AS select `users`.`email` AS `username`,`users`.`password` AS `password`,`users`.`attivo` AS `enabled` from `users` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `authorities`
--

/*!50001 DROP VIEW IF EXISTS `authorities`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `authorities` AS select distinct `authorities`.`id_user` AS `id_user`,`authorities`.`username` AS `username`,`authorities`.`authority` AS `authority`,`authorities`.`descrizione` AS `descrizione` from (select `authorities_from_permissions`.`id_user` AS `id_user`,`authorities_from_permissions`.`username` AS `username`,`authorities_from_permissions`.`authority` AS `authority`,`authorities_from_permissions`.`descrizione` AS `descrizione` from `authorities_from_permissions` union select `authorities_from_roles`.`id_user` AS `id_user`,`authorities_from_roles`.`username` AS `username`,`authorities_from_roles`.`authority` AS `authority`,`authorities_from_roles`.`descrizione` AS `descrizione` from `authorities_from_roles`) `authorities` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `authorities_from_permissions`
--

/*!50001 DROP VIEW IF EXISTS `authorities_from_permissions`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `authorities_from_permissions` AS select `users`.`id` AS `id_user`,`users`.`email` AS `username`,`permissions`.`nome` AS `authority`,`permissions`.`descrizione` AS `descrizione` from ((`users` join `users_permissions` on((`users`.`id` = `users_permissions`.`id_user`))) join `permissions` on((`permissions`.`nome` = `users_permissions`.`id_permission`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `authorities_from_roles`
--

/*!50001 DROP VIEW IF EXISTS `authorities_from_roles`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `authorities_from_roles` AS select distinct `users`.`id` AS `id_user`,`users`.`email` AS `username`,`permissions`.`nome` AS `authority`,`permissions`.`descrizione` AS `descrizione` from ((((`users` join `users_roles` on((`users`.`id` = `users_roles`.`id_user`))) join `roles` on((`roles`.`nome` = `users_roles`.`id_role`))) join `roles_permissions` on((`roles`.`nome` = `roles_permissions`.`id_role`))) join `permissions` on((`permissions`.`nome` = `roles_permissions`.`id_permission`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-03-10 15:41:50
