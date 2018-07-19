CREATE DATABASE  IF NOT EXISTS `wateringsystem_dynamic` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `wateringsystem_dynamic`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: wateringsystem_dynamic
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
INSERT INTO `environmental_factors` VALUES ('t1','r1','High','High','Fast','High','Low'),('t1','r2','Low','High','Slow','Low','High'),('t10','r1','Low','High','Fast','High','Low'),('t11','r1','Low','High','Fast','Low','Low'),('t12','r1','High','Low','Slow','Low','Low'),('t13','r1','Low','High','Fast','Low','High'),('t14','r1','High','Low','Fast','Low','Low'),('t15','r1','Low','Low','Slow','High','Low'),('t16','r1','High','Low','Slow','High','Low'),('t17','r1','High','High','Fast','High','Low'),('t18','r1','High','Low','Fast','High','Low'),('t19','r1','Low','High','Fast','Low','Low'),('t2','r1','High','High','Slow','Low','High'),('t20','r1','Low','Low','Slow','Low','High'),('t3','r1','High','Low','Slow','Low','Low'),('t4','r1','Low','Low','Slow','Low','Low'),('t5','r1','Low','High','Fast','High','High'),('t6','r1','High','Low','Slow','Low','High'),('t7','r1','High','Low','Slow','Low','Low'),('t8','r1','High','High','Slow','High','High'),('t9','r1','Low','High','Fast','High','Low');
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
INSERT INTO `land_state` VALUES ('r1','t1','High'),('r1','t10','Low'),('r1','t11','High'),('r1','t12','High'),('r1','t13','High'),('r1','t14','High'),('r1','t15','Low'),('r1','t16','Low'),('r1','t17','Low'),('r1','t18','Low'),('r1','t19','High'),('r1','t2','High'),('r1','t20','High'),('r1','t3','Low'),('r1','t4','Low'),('r1','t5','Low'),('r1','t6','High'),('r1','t7','Low'),('r1','t8','Low'),('r1','t9','Low'),('r2','t1','High'),('r2','t2','High');
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
-- Table structure for table `plant_state`
--

DROP TABLE IF EXISTS `plant_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `plant_state` (
  `idPlant` char(45) NOT NULL,
  `idTime` char(45) NOT NULL,
  `idRegion` char(45) NOT NULL,
  `Health` char(45) DEFAULT NULL,
  PRIMARY KEY (`idPlant`,`idTime`,`idRegion`),
  KEY `fk_Plant_Dosage_Time1_idx` (`idTime`),
  KEY `fk_plant_state_region1_idx` (`idRegion`),
  CONSTRAINT `fk_Plant_Dosage_Plant` FOREIGN KEY (`idPlant`) REFERENCES `plant` (`idPlant`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Plant_Dosage_Time1` FOREIGN KEY (`idTime`) REFERENCES `time` (`idTime`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_plant_state_region1` FOREIGN KEY (`idRegion`) REFERENCES `region` (`idRegion`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plant_state`
--

LOCK TABLES `plant_state` WRITE;
/*!40000 ALTER TABLE `plant_state` DISABLE KEYS */;
INSERT INTO `plant_state` VALUES ('p1','t1','r1','Good'),('p1','t10','r1','Good'),('p1','t11','r1','Good'),('p1','t12','r1','Bad'),('p1','t13','r1','Bad'),('p1','t14','r1','Bad'),('p1','t15','r1','Bad'),('p1','t16','r1','Bad'),('p1','t17','r1','Good'),('p1','t18','r1','Good'),('p1','t19','r1','Good'),('p1','t2','r1','Good'),('p1','t20','r1','Good'),('p1','t3','r1','Bad'),('p1','t4','r1','Bad'),('p1','t5','r1','Good'),('p1','t6','r1','Good'),('p1','t7','r1','Good'),('p1','t8','r1','Good'),('p1','t9','r1','Good'),('p2','t1','r2','Good');
/*!40000 ALTER TABLE `plant_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `predecessor`
--

DROP TABLE IF EXISTS `predecessor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `predecessor` (
  `idPrevTime` char(45) NOT NULL,
  `idTime` char(45) NOT NULL,
  PRIMARY KEY (`idPrevTime`,`idTime`),
  KEY `fk_predecessor_time2_idx` (`idTime`),
  CONSTRAINT `fk_predecessor_time1` FOREIGN KEY (`idPrevTime`) REFERENCES `time` (`idTime`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_predecessor_time2` FOREIGN KEY (`idTime`) REFERENCES `time` (`idTime`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `predecessor`
--

LOCK TABLES `predecessor` WRITE;
/*!40000 ALTER TABLE `predecessor` DISABLE KEYS */;
INSERT INTO `predecessor` VALUES ('t9','t10'),('t10','t11'),('t11','t12'),('t12','t13'),('t13','t14'),('t14','t15'),('t15','t16'),('t16','t17'),('t17','t18'),('t18','t19'),('t1','t2'),('t19','t20'),('t2','t3'),('t3','t4'),('t4','t5'),('t5','t6'),('t6','t7'),('t7','t8'),('t8','t9');
/*!40000 ALTER TABLE `predecessor` ENABLE KEYS */;
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
INSERT INTO `region` VALUES ('r1','Outdoor'),('r2','Indoor'),('r3','Indoor');
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
INSERT INTO `time` VALUES ('t1','Fall','H6_12'),('t10','Fall','H12_18'),('t11','Fall','H24_6'),('t12','Fall','H12_18'),('t13','Fall','H24_6'),('t14','Fall','H12_18'),('t15','Fall','H24_6'),('t16','Fall','H12_18'),('t17','Fall','H24_6'),('t18','Spring','H6_12'),('t19','Summer','H6_12'),('t2','Fall','H12_18'),('t20','Winter','H6_12'),('t3','Fall','H18_24'),('t4','Fall','H24_6'),('t5','Fall','H6_12'),('t6','Fall','H12_18'),('t7','Fall','H6_12'),('t8','Fall','H6_12'),('t9','Fall','H6_12');
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

-- Dump completed on 2018-07-19 19:50:40
