CREATE DATABASE  IF NOT EXISTS `spamdatabase` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `spamdatabase`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: spamdatabase
-- ------------------------------------------------------
-- Server version	5.7.21-log

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
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `review` (
  `idReview` varchar(20) NOT NULL,
  `od` varchar(1) DEFAULT NULL,
  `npp` varchar(1) DEFAULT NULL,
  `etf` varchar(1) DEFAULT NULL,
  `rd` varchar(1) DEFAULT NULL,
  `res` varchar(1) DEFAULT NULL,
  `spamicity` varchar(20) DEFAULT NULL,
  `idUser` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`idReview`),
  KEY `idUser` (`idUser`),
  CONSTRAINT `review_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES ('r10','T','T','F','T','T','high','u9'),('r11','T','F','T','F','T','high','u10'),('r12','T','F','F','T','T','high','u11'),('r13','F','F','T','F','F','low','u5'),('r14','F','F','F','T','F','low','u7'),('r15','F','F','T','T','F','low','u3'),('r16','F','T','T','T','F','high','u2'),('r2','F','F','F','F','F','low','u1'),('r3','T','F','T','F','T','high','u2'),('r4','T','F','F','F','F','high','u3'),('r5','T','T','T','T','T','high','u4'),('r6','F','F','F','F','F','low','u5'),('r7','F','T','T','F','T','low','u6'),('r8','T','T','F','T','T','high','u7'),('r9','F','T','T','F','T','low','u8');
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `idUser` varchar(20) NOT NULL,
  `bst` varchar(1) DEFAULT NULL,
  `nr` varchar(1) DEFAULT NULL,
  `acs` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`idUser`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('u1','T','T','F'),('u10','T','T','F'),('u11','T','T','T'),('u2','F','T','F'),('u3','T','T','T'),('u4','F','F','F'),('u5','T','F','T'),('u6','T','F','F'),('u7','F','F','T'),('u8','F','T','F'),('u9','F','F','F');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-02  3:00:16
