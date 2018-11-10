-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: fabrikainternabaza
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
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `idProduct` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  `price` double unsigned NOT NULL,
  `manufactureTime` int(10) unsigned NOT NULL,
  PRIMARY KEY (`idProduct`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Friskies meso za mace','meso',99,3000),(2,'Povodac kozni pleteni','povodac',1900,10000),(3,'Lopta kozna','lopta',600,4000),(4,'Tabla za pse','tabla',900,1000),(5,'Whiskas meso za mace','meso',100,2900),(6,'Sipina kost','kost',40,500),(7,'Josi cat meso','meso',60,3000),(8,'Povodac platneni','povodac',2000,3210),(9,'Lopta gumena','lopta',700,3000),(10,'Morningstar meso za pse','meso',150,3100),(11,'Kost za pse','kost',1200,3000),(12,'Povodac gumeni','povodac',1200,2300),(13,'Tylomax','lijek',5000,2120),(14,'Sanditan gel','lijek',6000,1201),(15,'Supitox sprej','lijek',1200,1230),(16,'Lopta za mace','lopta',510,1000),(17,'Kalcijum tablete za pse','tabla',780,1230),(18,'Fleatox prah za macke','lijek',1200,1200),(19,'Sampon pet care','sampon',1200,3000),(20,'Sampon pet skin','sampon',4100,3100),(21,'Felix govedina','meso',7000,1200),(22,'Felix jagnjetina','meso',7100,1400),(23,'Felix losos','meso',6500,1500),(24,'Kost sa kanapon','kost',400,1200),(25,'Antihlor','lijek',800,1400),(26,'Pileci batak','meso',1500,1300),(27,'Lopta na uzetu','lopta',1280,4000),(28,'Energy pasta sa taurinom','lijek',7880,4000),(29,'Sampon lightening','sampon',2890,2000),(30,'Cinija kost C016B','kost',1200,5000);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
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
