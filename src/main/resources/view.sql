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
-- Temporary view structure for view `mylife_last_data`
--

DROP TABLE IF EXISTS `mylife_last_data`;
/*!50001 DROP VIEW IF EXISTS `mylife_last_data`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `mylife_last_data` AS SELECT 
 1 AS `id`,
 1 AS `titolo`,
 1 AS `contenuto`,
 1 AS `foto`,
 1 AS `timestamp`,
 1 AS `tipo`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `mywork_last_data`
--

DROP TABLE IF EXISTS `mywork_last_data`;
/*!50001 DROP VIEW IF EXISTS `mywork_last_data`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `mywork_last_data` AS SELECT 
 1 AS `id`,
 1 AS `foto`,
 1 AS `titolo`,
 1 AS `contenuto`,
 1 AS `timestamp`,
 1 AS `luogo`,
 1 AS `tipo`*/;
SET character_set_client = @saved_cs_client;

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
/*!50001 VIEW `authorities` AS select `users`.`id` AS `id_user`,`users`.`email` AS `username`,`permissions`.`nome` AS `authority`,`permissions`.`descrizione` AS `descrizione` from ((`users` join `users_permissions` on((`users`.`id` = `users_permissions`.`id_user`))) join `permissions` on((`permissions`.`nome` = `users_permissions`.`id_permission`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `mylife_last_data`
--

/*!50001 DROP VIEW IF EXISTS `mylife_last_data`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `mylife_last_data` AS (select `aphorism`.`id` AS `id`,`aphorism`.`frase` AS `titolo`,`aphorism`.`autore` AS `contenuto`,NULL AS `foto`,NULL AS `timestamp`,'aphorism' AS `tipo` from `aphorism` where (`aphorism`.`is_life` = '1') order by `aphorism`.`id` desc limit 3) union (select `event`.`id` AS `id`,`event`.`titolo` AS `titolo`,`event`.`link` AS `contenuto`,(select `file_immagini`.`data` from `file_immagini` where (`file_immagini`.`id` = `event`.`id_immagine`)) AS `foto`,`event`.`data` AS `timestamp`,'eventi' AS `tipo` from `event` where (`event`.`visibile` = '1') order by `event`.`id` desc limit 2) union (select `cinema`.`id` AS `id`,`cinema`.`titolo` AS `titolo`,(select `cinema_category`.`nome` from `cinema_category` where (`cinema_category`.`id` = `cinema`.`id_categoria`)) AS `contenuto`,(select `file_immagini`.`data` from `file_immagini` where (`file_immagini`.`id` = `cinema`.`id_immagine`)) AS `foto`,NULL AS `timestamp`,'cinema' AS `tipo` from `cinema` where (`cinema`.`visibile` = '1') order by `cinema`.`id` desc limit 3) union (select `book`.`id` AS `id`,`book`.`titolo` AS `titolo`,`book`.`scrittore` AS `contenuto`,(select `file_immagini`.`data` from `file_immagini` where (`file_immagini`.`id` = `book`.`id_immagine`)) AS `foto`,NULL AS `timestamp`,'book' AS `tipo` from `book` where (`book`.`visibile` = '1') order by `book`.`id` desc limit 4) union (select `video`.`id` AS `id`,`video`.`sotto_titolo` AS `titolo`,`video`.`link` AS `contenuto`,NULL AS `timestamp`,NULL AS `foto`,'video' AS `tipo` from `video` where (`video`.`pagina` = 'MyLife') order by `video`.`id` desc limit 1) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `mywork_last_data`
--

/*!50001 DROP VIEW IF EXISTS `mywork_last_data`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `mywork_last_data` AS (select `aphorism`.`id` AS `id`,NULL AS `foto`,`aphorism`.`frase` AS `titolo`,`aphorism`.`autore` AS `contenuto`,NULL AS `timestamp`,NULL AS `luogo`,'aphorism' AS `tipo` from `aphorism` where (`aphorism`.`is_life` = '0') order by `aphorism`.`id` desc limit 3) union (select `news`.`id` AS `id`,(select `file_immagini`.`data` from `file_immagini` where (`file_immagini`.`id` = `news`.`id_immagine`)) AS `foto`,`news`.`titolo` AS `titolo`,`news`.`contenuto` AS `contenuto`,`news`.`data` AS `timestamp`,`news`.`luogo` AS `luogo`,'event' AS `tipo` from `news` where ((`news`.`type` = 1) and (`news`.`visibile` = '1')) order by `news`.`id` desc limit 3) union (select `news`.`id` AS `id`,(select `file_immagini`.`data` from `file_immagini` where (`file_immagini`.`id` = `news`.`id_immagine`)) AS `foto`,`news`.`titolo` AS `titolo`,`news`.`contenuto` AS `contenuto`,`news`.`data` AS `timestamp`,`news`.`luogo` AS `luogo`,'news' AS `tipo` from `news` where ((`news`.`type` = 0) and (`news`.`visibile` = '1')) order by `news`.`id` desc limit 3) union (select `sondaggio`.`id` AS `id`,NULL AS `foto`,`sondaggio`.`nome_sondaggio` AS `titolo`,`sondaggio`.`link` AS `contenuto`,`sondaggio`.`timestamp` AS `timestamp`,NULL AS `luogo`,'sondaggio' AS `tipo` from `sondaggio` where (`sondaggio`.`visibile` = '1') order by `sondaggio`.`id` desc limit 3) union (select `video`.`id` AS `id`,NULL AS `foto`,`video`.`sotto_titolo` AS `titolo`,`video`.`link` AS `contenuto`,NULL AS `timestamp`,NULL AS `luogo`,'video' AS `tipo` from `video` where (`video`.`pagina` = 'MyWork') order by `video`.`id` desc limit 1) union (select `client`.`id` AS `id`,(select `file_immagini`.`data` from `file_immagini` where (`file_immagini`.`id` = `client`.`id_immagine`)) AS `foto`,`client`.`nome` AS `titolo`,NULL AS `contenuto`,`client`.`data_inizio` AS `timestamp`,NULL AS `luogo`,'cliente' AS `tipo` from `client` where (`client`.`visibile` = '1') order by `client`.`id` desc limit 3) */;
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

-- Dump completed on 2021-04-15  8:47:16
