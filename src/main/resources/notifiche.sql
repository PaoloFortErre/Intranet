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
-- Table structure for table `notifica`
--

DROP TABLE IF EXISTS `notifica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifica` (
  `id` int NOT NULL,
  `descrizione` varchar(255) DEFAULT NULL,
  `timestamp` bigint NOT NULL,
  `utente` tinyblob,
  `id_autore` int DEFAULT NULL,
  `destinazione` varchar(255) DEFAULT NULL,
  `id_dest` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo99vmu195otps116malyobj2t` (`id_autore`),
  CONSTRAINT `FKo99vmu195otps116malyobj2t` FOREIGN KEY (`id_autore`) REFERENCES `dati_utente` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifica`
--

LOCK TABLES `notifica` WRITE;
/*!40000 ALTER TABLE `notifica` DISABLE KEYS */;
INSERT INTO `notifica` VALUES (966,'ha commentato il tuo post',1617799600,NULL,1,NULL,0),(987,'ha inserito un nuovo video su MyLife!',1617890292,NULL,1,NULL,0),(988,'ha inserito un nuovo video su MyLife!',1617890354,NULL,1,NULL,0),(989,'ha inserito un nuovo video su MyLife!',1617890445,NULL,1,NULL,0),(990,'ha inserito un nuovo video su MyLife!',1617890508,NULL,1,NULL,0),(991,'ha inserito un nuovo video su MyLife!',1617890627,NULL,1,NULL,0),(997,'ha inserito una news su MyWork!',1617954480,NULL,1,NULL,0),(1009,'ha inserito un nuovo film consigliato dalla redazione!',1617959015,NULL,54,'MyLife',0),(1014,'ha inserito un nuovo film consigliato dalla redazione!',1617959182,NULL,54,'MyLife',0),(1017,'ha inserito un nuovo video su MyWork!',1617959373,NULL,54,'MyWork',0);
/*!40000 ALTER TABLE `notifica` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-09 11:12:42
