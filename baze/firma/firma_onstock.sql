-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: firma
-- ------------------------------------------------------
-- Server version	5.7.20-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `onstock`
--

DROP TABLE IF EXISTS `onstock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `onstock` (
  `quantity` int(10) unsigned NOT NULL,
  `idArticle` int(11) NOT NULL,
  `idStore` int(11) NOT NULL,
  PRIMARY KEY (`idArticle`,`idStore`),
  KEY `idProdavnica_idx` (`idStore`),
  CONSTRAINT `articleId` FOREIGN KEY (`idArticle`) REFERENCES `article` (`idArticle`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `storeId` FOREIGN KEY (`idStore`) REFERENCES `store` (`idStore`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `onstock`
--

LOCK TABLES `onstock` WRITE;
/*!40000 ALTER TABLE `onstock` DISABLE KEYS */;
INSERT INTO `onstock` VALUES (50,1,1),(307,1,2),(70,1,5),(120,1,6),(27,1,7),(23,1,9),(32,1,10),(1227,1,12),(4,2,1),(2,2,2),(54,2,7),(234,2,9),(0,3,1),(2428,3,3),(40,3,7),(44,3,8),(100,3,10),(435,3,12),(43,4,9),(12,4,10),(43,4,11),(76,5,8),(120,6,4),(12,6,7),(10,7,4),(90,7,8),(43,7,9),(144,7,11),(45,7,12),(32,8,7),(80,9,9),(71,10,10),(564,11,8),(12,12,12),(177,13,7),(168,17,5),(76,17,9),(32,17,10),(43,17,12),(54,18,8),(71,20,8),(53,21,8),(56,21,10),(124,24,7),(43,25,10),(61,25,11),(129,26,4),(12,26,7),(14,27,1),(17,27,2),(123,27,6),(124,28,5),(80,29,1),(24,30,8);
/*!40000 ALTER TABLE `onstock` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-26 16:49:29
