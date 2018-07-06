CREATE DATABASE  IF NOT EXISTS `wateringsystem` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `wateringsystem`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: wateringsystem
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
-- Table structure for table `environmental_factors`
--

DROP TABLE IF EXISTS `environmental_factors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `environmental_factors` (
  `idTime` char(45) NOT NULL,
  `idRegion` char(45) NOT NULL,
  `Temperature` char(45) DEFAULT NULL,
  `Light` char(45) DEFAULT NULL,
  `Wind` char(45) DEFAULT NULL,
  `Humidity` char(45) DEFAULT NULL,
  `Rain` char(45) DEFAULT NULL,
  PRIMARY KEY (`idTime`,`idRegion`),
  KEY `fk_table5_Region1_idx` (`idRegion`),
  CONSTRAINT `fk_table5_Region1` FOREIGN KEY (`idRegion`) REFERENCES `region` (`idRegion`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_table5_Time1` FOREIGN KEY (`idTime`) REFERENCES `time` (`idTime`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `environmental_factors`
--

LOCK TABLES `environmental_factors` WRITE;
/*!40000 ALTER TABLE `environmental_factors` DISABLE KEYS */;
INSERT INTO `environmental_factors` VALUES ('t1','r1','High','High','Fast','High','High'),('t1','r2','Low','High','Slow','Low','High'),('t2','r1','High','High','Slow','High','Low'),('t2','r2','High','Low','Fast','High','Low'),('t3','r1','Low','Low','Fast','Low','Low'),('t3','r2','High','High','Slow','High','High');
/*!40000 ALTER TABLE `environmental_factors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `land_state`
--

DROP TABLE IF EXISTS `land_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `land_state` (
  `idRegion` char(45) NOT NULL,
  `idTime` char(45) NOT NULL,
  `Dry` char(45) DEFAULT NULL,
  PRIMARY KEY (`idRegion`,`idTime`),
  KEY `fk_table6_Time1_idx` (`idTime`),
  CONSTRAINT `fk_table6_Region1` FOREIGN KEY (`idRegion`) REFERENCES `region` (`idRegion`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_table6_Time1` FOREIGN KEY (`idTime`) REFERENCES `time` (`idTime`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `land_state`
--

LOCK TABLES `land_state` WRITE;
/*!40000 ALTER TABLE `land_state` DISABLE KEYS */;
INSERT INTO `land_state` VALUES ('r1','t1','High'),('r1','t2','Low'),('r1','t3','Low'),('r2','t1','Low'),('r2','t2','High'),('r2','t3','High'),('r3','t1','High'),('r3','t2','High'),('r3','t3','High');
/*!40000 ALTER TABLE `land_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plant`
--

DROP TABLE IF EXISTS `plant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `plant` (
  `idPlant` char(45) NOT NULL,
  `PlantType` char(45) DEFAULT NULL,
  PRIMARY KEY (`idPlant`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plant`
--

LOCK TABLES `plant` WRITE;
/*!40000 ALTER TABLE `plant` DISABLE KEYS */;
INSERT INTO `plant` VALUES ('p1','GardenPlant'),('p2','GardenPlant'),('p3','Others');
/*!40000 ALTER TABLE `plant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plant_dosage`
--

DROP TABLE IF EXISTS `plant_dosage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `plant_dosage` (
  `idPlant` char(45) NOT NULL,
  `idTime` char(45) NOT NULL,
  `Dosage` char(45) DEFAULT NULL,
  PRIMARY KEY (`idPlant`,`idTime`),
  KEY `fk_Plant_Dosage_Time1_idx` (`idTime`),
  CONSTRAINT `fk_Plant_Dosage_Plant` FOREIGN KEY (`idPlant`) REFERENCES `plant` (`idPlant`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Plant_Dosage_Time1` FOREIGN KEY (`idTime`) REFERENCES `time` (`idTime`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plant_dosage`
--

LOCK TABLES `plant_dosage` WRITE;
/*!40000 ALTER TABLE `plant_dosage` DISABLE KEYS */;
INSERT INTO `plant_dosage` VALUES ('p1','t1','High'),('p1','t2','High'),('p1','t3','Low'),('p2','t1','High'),('p2','t2','High'),('p2','t3','Low'),('p3','t1','Low'),('p3','t2','Low'),('p3','t3','High');
/*!40000 ALTER TABLE `plant_dosage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `region`
--

DROP TABLE IF EXISTS `region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `region` (
  `idRegion` char(45) NOT NULL,
  `LocationType` char(45) DEFAULT NULL,
  PRIMARY KEY (`idRegion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `region`
--

LOCK TABLES `region` WRITE;
/*!40000 ALTER TABLE `region` DISABLE KEYS */;
INSERT INTO `region` VALUES ('r1','Indoor'),('r2','Indoor'),('r3','Outdoor');
/*!40000 ALTER TABLE `region` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `time`
--

DROP TABLE IF EXISTS `time`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `time` (
  `idTime` char(45) NOT NULL,
  `Season` char(45) DEFAULT NULL,
  `DayTime` char(45) DEFAULT NULL,
  PRIMARY KEY (`idTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time`
--

LOCK TABLES `time` WRITE;
/*!40000 ALTER TABLE `time` DISABLE KEYS */;
INSERT INTO `time` VALUES ('t1','Fall','H6_12'),('t2','Fall','H12_18'),('t3','Fall','H18_24'),('t4','Fall','H18_24'),('t5','Fall','H24_6'),('t6','Spring','H18_24'),('t7','Summer','H18_24'),('t8','Winter','H18_24'),('t9','Winter','H18_24');
/*!40000 ALTER TABLE `time` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-07-06 18:18:34
