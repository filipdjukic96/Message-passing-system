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
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article` (
  `idArticle` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  `price` double unsigned NOT NULL,
  PRIMARY KEY (`idArticle`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES (1,'Friskies meso za mace','meso',99),(2,'Povodac kozni pleteni','povodac',1900),(3,'Lopta kozna','lopta',600),(4,'Tabla za pse','tabla',900),(5,'Whiskas meso za mace','meso',100),(6,'Sipina kost','kost',40),(7,'Josi cat meso','meso',60),(8,'Povodac platneni','povodac',2000),(9,'Lopta gumena','lopta',700),(10,'Morningstar meso za pse','meso',150),(11,'Kost za pse','kost',1200),(12,'Povodac gumeni','povodac',1200),(13,'Tylomax','lijek',5000),(14,'Sanditan gel','lijek',6000),(15,'Supitox sorei','lijek',1200),(16,'Lopta za mace','lopta',510),(17,'Kalcijum tablete za pse','tabla',780),(18,'Fleatox prah za macke','lijek',120),(19,'Sampon pet care','sampon',1200),(20,'Sampon pet skin','sampon',4100),(21,'Felix govedina','meso',7000),(22,'Felix jagnjetina','meso',7100),(23,'Felix losos','meso',6500),(24,'Kost sa kanapom','kost',400),(25,'Antihlor','lijek',800),(26,'Pileci batak','meso',1500),(27,'Lopta na uzetu','lopta',1280),(28,'Energy pasta sa taurinom ','lijek',7880),(29,'Sampon lightening','sampon ',2890),(30,'Cinija kost C016B','kost',1200);
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
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
