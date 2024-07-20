-- MySQL dump 10.13  Distrib 8.0.31, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: mysql
-- ------------------------------------------------------
-- Server version	8.0.31-google

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `DentistryManagementSystem`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `DentistryManagementSystem` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `DentistryManagementSystem`;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
  `appointmentid` varchar(255) NOT NULL,
  `date` date DEFAULT NULL,
  `feedback` varchar(255) DEFAULT NULL,
  `status` int NOT NULL,
  `clinicid` varchar(255) NOT NULL,
  `dentistid` varchar(255) NOT NULL,
  `dependentid` varchar(255) DEFAULT NULL,
  `serviceid` varchar(255) NOT NULL,
  `staffid` varchar(255) DEFAULT NULL,
  `time_slotid` varchar(255) NOT NULL,
  `customerid` varchar(255) NOT NULL,
  `dentist_schedule_id` varchar(255) DEFAULT NULL,
  `star_appointment` int NOT NULL,
  PRIMARY KEY (`appointmentid`),
  KEY `FK1glfaftvveygaeivkloo45w2i` (`clinicid`),
  KEY `FKa37fadv7f00ahl0ajk4jou7cq` (`dentistid`),
  KEY `FKa9ijoiy6fg9gtf8m96cx19jrj` (`dependentid`),
  KEY `FKat44efj102e2el2owkduwtvm2` (`serviceid`),
  KEY `FK2ovnnl6dm5hndlexj1kw34ixi` (`staffid`),
  KEY `FKt5oi81l7srd1i34s09bj6u1bf` (`customerid`),
  KEY `FKnouxscrsfbk2o73klxuumk77p` (`time_slotid`),
  CONSTRAINT `FK1glfaftvveygaeivkloo45w2i` FOREIGN KEY (`clinicid`) REFERENCES `clinic` (`clinicid`),
  CONSTRAINT `FK2ovnnl6dm5hndlexj1kw34ixi` FOREIGN KEY (`staffid`) REFERENCES `staff` (`staffid`),
  CONSTRAINT `FKa37fadv7f00ahl0ajk4jou7cq` FOREIGN KEY (`dentistid`) REFERENCES `dentist` (`dentistid`),
  CONSTRAINT `FKa9ijoiy6fg9gtf8m96cx19jrj` FOREIGN KEY (`dependentid`) REFERENCES `dependent` (`dependentid`),
  CONSTRAINT `FKat44efj102e2el2owkduwtvm2` FOREIGN KEY (`serviceid`) REFERENCES `service` (`serviceid`),
  CONSTRAINT `FKnouxscrsfbk2o73klxuumk77p` FOREIGN KEY (`time_slotid`) REFERENCES `time_slot` (`time_slotid`),
  CONSTRAINT `FKt5oi81l7srd1i34s09bj6u1bf` FOREIGN KEY (`customerid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES ('070777b8-2cf6-41ae-96e1-af9d9487b9c4','2024-07-17',NULL,1,'ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50',NULL,'9713f279-2876-11ef-9d25-42010a400003',NULL,'77cf23b5-a974-4c2e-9a9c-bee6fed96749','f4287374-5092-4023-860b-f8f362e65e69','2b290c4e-ff26-42c9-aaa3-3446681f571a',0),('2f969bee-699b-4c84-8e2e-581e6071132e','2024-07-13',NULL,2,'ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','64ce203a-300a-4b80-b591-836997b72c53','9713f323-2876-11ef-9d25-42010a400003',NULL,'77cf23b5-a974-4c2e-9a9c-bee6fed96749','aa4f09b5-5bd6-4f7c-a780-28f7ca8ebf2a','ca44747d-2f2e-490d-8cd3-f1ec3176dd81',0),('2f997116-34f0-49a6-a612-589bb843a1d8','2024-07-18',NULL,1,'ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','64ce203a-300a-4b80-b591-836997b72c53','9713f384-2876-11ef-9d25-42010a400003',NULL,'454feda3-00a1-415c-870c-5b3c918fb915','aa4f09b5-5bd6-4f7c-a780-28f7ca8ebf2a','fe9be3a5-ac31-46a2-bf35-8b5032fe83ab',0),('301f7b54-138c-4508-aa67-3af376fdc5f7','2024-07-16',NULL,0,'ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9',NULL,'9713e2bb-2876-11ef-9d25-42010a400003',NULL,'454feda3-00a1-415c-870c-5b3c918fb915','828ac80f-0d5d-43ec-b385-c5da046ebb00','02601f4e-95fa-4e60-972d-c26ecd411295',0),('36779bad-5f9a-405a-940b-aea0f6363bb4','2024-07-20',NULL,1,'ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9',NULL,'9713f384-2876-11ef-9d25-42010a400003',NULL,'631e5984-9d03-4569-9a08-c20bafac4df3','c713973c-dc0e-4216-8ad5-c689dce39767','d307e841-3b0a-4f41-92a4-b9458e3f74b7',0),('41d2bd4e-2caf-4be0-a1b9-c5a657298c28','2024-07-16',NULL,0,'ebca4142-7767-4422-a6e3-09b16d91bdc9','20386020-1073-4dc7-a115-36329149110b',NULL,'9713e2bb-2876-11ef-9d25-42010a400003',NULL,'77cf23b5-a974-4c2e-9a9c-bee6fed96749','fdeb74f7-c20c-4176-8310-37bd1c506d0b','69ba444a-ffcc-46b3-84f6-95bcabd008a1',0),('42701f0d-0578-4b34-b8e4-b062d525bbf8','2024-07-21',NULL,1,'ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','2f35f3ea-4c0c-4421-86a5-a7fe8828b562','9713f384-2876-11ef-9d25-42010a400003','e0434665-35c2-4d28-83ed-91a780b5235e','454feda3-00a1-415c-870c-5b3c918fb915','9e0ba96c-a598-4d43-9c96-68aaf3f4ac54','1996d3a4-746e-4d50-9f67-4cd89e5c514d',0),('51aa4ca0-36d8-46cb-9a7d-7265f8a87ac0','2024-07-16',NULL,0,'ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9',NULL,'9713e2bb-2876-11ef-9d25-42010a400003',NULL,'454feda3-00a1-415c-870c-5b3c918fb915','828ac80f-0d5d-43ec-b385-c5da046ebb00','02601f4e-95fa-4e60-972d-c26ecd411295',0),('57a15045-85b6-425c-b191-aab3a1047556','2024-07-19',NULL,1,'ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9',NULL,'9713f384-2876-11ef-9d25-42010a400003',NULL,'454feda3-00a1-415c-870c-5b3c918fb915','c713973c-dc0e-4216-8ad5-c689dce39767','4e53a2de-0b27-4ba7-9795-40595af550f4',0),('5eb4fee6-2923-495f-8cee-c32cc23d1401','2024-07-23',NULL,0,'ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','90255347-18fe-4e64-a681-053a665b92fa','9713f384-2876-11ef-9d25-42010a400003',NULL,'454feda3-00a1-415c-870c-5b3c918fb915','f4287374-5092-4023-860b-f8f362e65e69','d8d93cfe-7552-4309-b1d2-32d77d052398',0),('67569811-3fa3-4a35-8ede-1fc627c01d1a','2024-07-16',NULL,0,'ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50',NULL,'9713f356-2876-11ef-9d25-42010a400003',NULL,'77cf23b5-a974-4c2e-9a9c-bee6fed96749','fdeb74f7-c20c-4176-8310-37bd1c506d0b','b45450f7-985b-48a9-80dc-a87dfe7e0db5',0),('69d91230-cf83-4e94-b414-c06908cb944c','2024-07-16',NULL,0,'ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50',NULL,'9713f356-2876-11ef-9d25-42010a400003',NULL,'77cf23b5-a974-4c2e-9a9c-bee6fed96749','fdeb74f7-c20c-4176-8310-37bd1c506d0b','b45450f7-985b-48a9-80dc-a87dfe7e0db5',0),('8fcb3ef4-eff6-4890-a8ac-779e1299588d','2024-07-18',NULL,1,'ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50',NULL,'9713f279-2876-11ef-9d25-42010a400003','e0434665-35c2-4d28-83ed-91a780b5235e','77cf23b5-a974-4c2e-9a9c-bee6fed96749','f4287374-5092-4023-860b-f8f362e65e69','b7dfd500-5c4f-48e0-a455-c72b420b155d',0),('9131cde6-efaf-4007-a3d3-f45d33b84d9e','2024-07-14',NULL,1,'ebca4142-7767-4422-a6e3-09b16d91bdc9','20386020-1073-4dc7-a115-36329149110b',NULL,'9713e2bb-2876-11ef-9d25-42010a400003',NULL,'e88f54c9-b1ea-4525-92b8-f2cb46013699','f4287374-5092-4023-860b-f8f362e65e69','b708e5af-4652-4f9a-976c-2d0709efd919',0),('9890313f-1dc6-4d7a-99ec-6d00df82a015','2024-07-16',NULL,0,'ebca4142-7767-4422-a6e3-09b16d91bdc9','20386020-1073-4dc7-a115-36329149110b',NULL,'9713e2bb-2876-11ef-9d25-42010a400003',NULL,'77cf23b5-a974-4c2e-9a9c-bee6fed96749','fdeb74f7-c20c-4176-8310-37bd1c506d0b','69ba444a-ffcc-46b3-84f6-95bcabd008a1',0),('a5cade36-ae41-4797-81c9-54a9c8914dad','2024-07-20',NULL,1,'ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50',NULL,'9713f384-2876-11ef-9d25-42010a400003',NULL,'77cf23b5-a974-4c2e-9a9c-bee6fed96749','c713973c-dc0e-4216-8ad5-c689dce39767','7edf16b9-5d77-4514-b581-ea9563ecb1d4',0),('ab07115a-8175-43b0-b3c8-1a80d316f219','2024-07-20',NULL,1,'ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9',NULL,'9713f384-2876-11ef-9d25-42010a400003',NULL,'454feda3-00a1-415c-870c-5b3c918fb915','c713973c-dc0e-4216-8ad5-c689dce39767','ec1222cc-6fb9-4dc6-8469-93570e7c9d82',0),('ab2137fd-c7e5-4ceb-ae53-402fe7cf00ce','2024-07-16',NULL,0,'ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50',NULL,'9713e2bb-2876-11ef-9d25-42010a400003',NULL,'77cf23b5-a974-4c2e-9a9c-bee6fed96749','fdeb74f7-c20c-4176-8310-37bd1c506d0b','b45450f7-985b-48a9-80dc-a87dfe7e0db5',0),('bd35486d-f360-4728-b3e7-702437bd2a7b','2024-07-24',NULL,1,'ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9',NULL,'9713f384-2876-11ef-9d25-42010a400003','e0434665-35c2-4d28-83ed-91a780b5235e','454feda3-00a1-415c-870c-5b3c918fb915','aa4f09b5-5bd6-4f7c-a780-28f7ca8ebf2a','ad4497ba-d25a-453e-8f99-a59d60967062',0),('e06c0132-779f-4a13-b01f-2c21466e4605','2024-07-16',NULL,0,'ebca4142-7767-4422-a6e3-09b16d91bdc9','20386020-1073-4dc7-a115-36329149110b',NULL,'9713e2bb-2876-11ef-9d25-42010a400003',NULL,'77cf23b5-a974-4c2e-9a9c-bee6fed96749','17cfd57a-2902-4719-b6ea-dfbc722ac422','69ba444a-ffcc-46b3-84f6-95bcabd008a1',0),('ed4e4ca6-38e3-4ffd-975e-8d1202123686','2024-07-14','',2,'ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50',NULL,'9713f356-2876-11ef-9d25-42010a400003','e0434665-35c2-4d28-83ed-91a780b5235e','77cf23b5-a974-4c2e-9a9c-bee6fed96749','c713973c-dc0e-4216-8ad5-c689dce39767','1ec3398c-8dde-4a3d-8b3a-971cdfcbe30b',4),('ed85bcd4-9bab-4bfa-b5c6-a56e34fa748d','2024-07-16',NULL,1,'ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50',NULL,'9713e2bb-2876-11ef-9d25-42010a400003',NULL,'77cf23b5-a974-4c2e-9a9c-bee6fed96749','fdeb74f7-c20c-4176-8310-37bd1c506d0b','b45450f7-985b-48a9-80dc-a87dfe7e0db5',0),('f06f9c3a-db1f-4b8c-b2fc-1cf436baccf3','2024-07-13','My teeth is more real than a driller',2,'ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9',NULL,'9713f384-2876-11ef-9d25-42010a400003','e0434665-35c2-4d28-83ed-91a780b5235e','631e5984-9d03-4569-9a08-c20bafac4df3','e74aefdc-f486-4b05-89ee-bb65cd52c613','3478c834-b9b9-460d-96d1-eaa07645ff78',5),('fd8ba03e-6610-42a3-91af-d44fbc8cf0e4','2024-07-18',NULL,1,'ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9',NULL,'9713f384-2876-11ef-9d25-42010a400003',NULL,'631e5984-9d03-4569-9a08-c20bafac4df3','aa4f09b5-5bd6-4f7c-a780-28f7ca8ebf2a','15d4efd2-3a92-4900-a7f6-88ce2343056d',0);
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clinic`
--

DROP TABLE IF EXISTS `clinic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clinic` (
  `clinicid` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `break_end_time` time(6) DEFAULT NULL,
  `break_start_time` time(6) DEFAULT NULL,
  `close_time` time(6) DEFAULT NULL,
  `open_time` time(6) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `slot_duration` time(6) DEFAULT NULL,
  `userid` varchar(255) NOT NULL,
  `status` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`clinicid`),
  KEY `FK4g5ygx4vw3jbx9oo3qe7bh1s7` (`userid`),
  CONSTRAINT `FK4g5ygx4vw3jbx9oo3qe7bh1s7` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clinic`
--

LOCK TABLES `clinic` WRITE;
/*!40000 ALTER TABLE `clinic` DISABLE KEYS */;
INSERT INTO `clinic` VALUES ('307f9101-7c0b-4ad1-a0c3-3c43104c2534','123 Phan Van Tri','13:00:00.000000','12:00:00.000000','17:00:00.000000','07:00:00.000000','0909164738','00:30:00.000000','2f13f8be-167b-4e05-970c-e8c21dde554b',0,'clinic 1'),('52ff9e26-b14a-403b-aabd-e21698c03393','45 Phan Chu Trinh','06:00:00.000000','04:00:00.000000','17:00:00.000000','01:00:00.000000','04539856718','00:30:00.000000','2f13f8be-167b-4e05-970c-e8c21dde554b',0,'clinic 2'),('56f70ac4-e415-484f-99a1-2f76d1b30bc7','67 Tran Hung Dao','12:00:00.000000','11:00:00.000000','21:00:00.000000','08:00:00.000000','09728354671','00:30:00.000000','ad83ff05-4ab1-4fff-9126-2c7f68971d9f',1,'clinic 6'),('757c247c-6968-43c1-98dd-e1837b0e3b16','12/7 Nguyen Quang Sang','13:30:00.000000','11:30:00.000000','21:30:00.000000','06:00:00.000000','0456897367','00:30:00.000000','2f13f8be-167b-4e05-970c-e8c21dde554b',1,'clinic 3'),('a7584e4b-6a07-448d-bea6-51ac2107a044','27 Le Van Luyen','13:00:00.000000','12:00:00.000000','17:00:00.000000','07:00:00.000000','06784284617','01:00:00.000000','2f13f8be-167b-4e05-970c-e8c21dde554b',0,'clinic 4'),('ebca4142-7767-4422-a6e3-09b16d91bdc9','99 Thach That','12:30:00.000000','11:30:00.000000','20:30:00.000000','07:00:00.000000','02639845671','00:30:00.000000','2f13f8be-167b-4e05-970c-e8c21dde554b',1,'clinic 5');
/*!40000 ALTER TABLE `clinic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dentist`
--

DROP TABLE IF EXISTS `dentist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dentist` (
  `dentistid` varchar(255) NOT NULL,
  `clinicid` varchar(255) NOT NULL,
  `staffid` varchar(255) NOT NULL,
  PRIMARY KEY (`dentistid`),
  KEY `FKgblhmmmbyvqyesaihcgt3q4ly` (`clinicid`),
  KEY `dentist_staff__fk` (`staffid`),
  CONSTRAINT `dentist_staff__fk` FOREIGN KEY (`staffid`) REFERENCES `staff` (`staffid`),
  CONSTRAINT `FKgblhmmmbyvqyesaihcgt3q4ly` FOREIGN KEY (`clinicid`) REFERENCES `clinic` (`clinicid`),
  CONSTRAINT `FKh27u6latjr4vpccxucdehrja5` FOREIGN KEY (`dentistid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dentist`
--

LOCK TABLES `dentist` WRITE;
/*!40000 ALTER TABLE `dentist` DISABLE KEYS */;
INSERT INTO `dentist` VALUES ('06b917d4-acc5-4533-be59-06ac4fe962fc','56f70ac4-e415-484f-99a1-2f76d1b30bc7','21f799d6-b102-4792-9e13-030ed7a79502'),('20386020-1073-4dc7-a115-36329149110b','ebca4142-7767-4422-a6e3-09b16d91bdc9','e0434665-35c2-4d28-83ed-91a780b5235e'),('40ab0249-e04d-44c4-a40b-bc818820f71e','56f70ac4-e415-484f-99a1-2f76d1b30bc7','21f799d6-b102-4792-9e13-030ed7a79502'),('4b952cf7-7e24-4b72-8d86-140d2a603c4c','56f70ac4-e415-484f-99a1-2f76d1b30bc7','21f799d6-b102-4792-9e13-030ed7a79502'),('73dcc41d-3ff7-487f-83af-d3df5ed2d4f9','56f70ac4-e415-484f-99a1-2f76d1b30bc7','21f799d6-b102-4792-9e13-030ed7a79502'),('83cdbcd4-de94-428f-a72d-eb1b42fe8d9c','56f70ac4-e415-484f-99a1-2f76d1b30bc7','21f799d6-b102-4792-9e13-030ed7a79502'),('88ac11e2-0aa2-4582-a697-5cbe3e8b199c','56f70ac4-e415-484f-99a1-2f76d1b30bc7','21f799d6-b102-4792-9e13-030ed7a79502'),('93a3d42d-0f2a-4ebb-86b6-9fd8b11116f4','56f70ac4-e415-484f-99a1-2f76d1b30bc7','21f799d6-b102-4792-9e13-030ed7a79502'),('9a6cc0a5-7774-4acd-b6a1-9111140f7421','56f70ac4-e415-484f-99a1-2f76d1b30bc7','21f799d6-b102-4792-9e13-030ed7a79502'),('b7c4c1d2-0fd9-42cc-a1c8-028dc7bde79c','56f70ac4-e415-484f-99a1-2f76d1b30bc7','21f799d6-b102-4792-9e13-030ed7a79502'),('c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','ebca4142-7767-4422-a6e3-09b16d91bdc9','e0434665-35c2-4d28-83ed-91a780b5235e'),('d306f39b-e877-41b4-994f-eb82fac44113','56f70ac4-e415-484f-99a1-2f76d1b30bc7','21f799d6-b102-4792-9e13-030ed7a79502'),('e6253a31-b770-4028-8d6d-6208bcec9ea9','ebca4142-7767-4422-a6e3-09b16d91bdc9','e0434665-35c2-4d28-83ed-91a780b5235e');
/*!40000 ALTER TABLE `dentist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dentist_schedule`
--

DROP TABLE IF EXISTS `dentist_schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dentist_schedule` (
  `scheduleid` varchar(255) NOT NULL,
  `available` int NOT NULL DEFAULT '1',
  `work_date` date DEFAULT NULL,
  `clinicid` varchar(255) NOT NULL,
  `dentistid` varchar(255) DEFAULT NULL,
  `time_slotid` varchar(255) NOT NULL,
  PRIMARY KEY (`scheduleid`),
  KEY `FK13drfd7od0149q9aq3yp2xvwf` (`clinicid`),
  KEY `FK9lh9gsu18i234k6aotr5aib0j` (`dentistid`),
  KEY `fk_timeSlotID` (`time_slotid`),
  CONSTRAINT `FK13drfd7od0149q9aq3yp2xvwf` FOREIGN KEY (`clinicid`) REFERENCES `clinic` (`clinicid`),
  CONSTRAINT `FK9lh9gsu18i234k6aotr5aib0j` FOREIGN KEY (`dentistid`) REFERENCES `dentist` (`dentistid`),
  CONSTRAINT `fk_timeSlotID` FOREIGN KEY (`time_slotid`) REFERENCES `time_slot` (`time_slotid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dentist_schedule`
--

LOCK TABLES `dentist_schedule` WRITE;
/*!40000 ALTER TABLE `dentist_schedule` DISABLE KEYS */;
INSERT INTO `dentist_schedule` VALUES ('0336371c-283b-427e-b92d-30eef2ee7795',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','93a3d42d-0f2a-4ebb-86b6-9fd8b11116f4','58b0a54c-e5ac-45a7-a2a9-7456545c9125'),('13635407-e3b5-458d-8671-710ec34cf7bb',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','93a3d42d-0f2a-4ebb-86b6-9fd8b11116f4','c654bfef-3d91-4b53-92d1-dd162b50618a'),('15d4efd2-3a92-4900-a7f6-88ce2343056d',0,'2024-07-18','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','631e5984-9d03-4569-9a08-c20bafac4df3'),('1996d3a4-746e-4d50-9f67-4cd89e5c514d',0,'2024-07-21','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('1ec3398c-8dde-4a3d-8b3a-971cdfcbe30b',0,'2024-07-14','ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','77cf23b5-a974-4c2e-9a9c-bee6fed96749'),('1f8ce1d4-17f8-4e44-bfc9-7ead43debb71',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','4b952cf7-7e24-4b72-8d86-140d2a603c4c','7f94b24f-913a-43d2-a2c7-d3441e6bce89'),('2379c581-d6a0-4f5a-8faf-c21bc1e1297a',0,'2024-07-14','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('2b290c4e-ff26-42c9-aaa3-3446681f571a',0,'2024-07-17','ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','77cf23b5-a974-4c2e-9a9c-bee6fed96749'),('2c800dcf-41d7-4793-bb8f-1dc52c0b35f6',0,'2024-07-19','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','631e5984-9d03-4569-9a08-c20bafac4df3'),('3478c834-b9b9-460d-96d1-eaa07645ff78',0,'2024-07-13','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','631e5984-9d03-4569-9a08-c20bafac4df3'),('38ef7f3f-740e-4a80-9896-a4032475ea76',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','88ac11e2-0aa2-4582-a697-5cbe3e8b199c','0ddef3f3-3769-4f0c-a1af-6bc512995f98'),('3b2abb91-7765-4fde-a4e1-f6e5d07d246d',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','40ab0249-e04d-44c4-a40b-bc818820f71e','c43bf1ab-5e68-4b76-a071-1fd3341ca058'),('4e53a2de-0b27-4ba7-9795-40595af550f4',0,'2024-07-19','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('5498c06a-29f3-4fac-a7b8-678552365959',1,'2024-07-26','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('5def288f-b671-47b9-8ebe-452f1d4dbbea',1,'2024-08-01','56f70ac4-e415-484f-99a1-2f76d1b30bc7','93a3d42d-0f2a-4ebb-86b6-9fd8b11116f4','c654bfef-3d91-4b53-92d1-dd162b50618a'),('6171072b-19c5-486a-b951-0a5ec19bca11',1,'2024-07-31','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('66a1aa2e-66d8-4916-8f41-908fd1d5bded',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','4b952cf7-7e24-4b72-8d86-140d2a603c4c','58b0a54c-e5ac-45a7-a2a9-7456545c9125'),('7607e9f5-1da9-41a6-ba5e-2303e5910d48',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','9a6cc0a5-7774-4acd-b6a1-9111140f7421','58b0a54c-e5ac-45a7-a2a9-7456545c9125'),('7edf16b9-5d77-4514-b581-ea9563ecb1d4',0,'2024-07-20','ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','77cf23b5-a974-4c2e-9a9c-bee6fed96749'),('8b1b639b-3e42-4611-a763-3658311fa8b9',0,'2024-07-27','56f70ac4-e415-484f-99a1-2f76d1b30bc7','06b917d4-acc5-4533-be59-06ac4fe962fc','5e78ee7e-3603-4246-901b-54b2b83c1343'),('9736bb55-bfd1-4295-a340-f9d308dab214',1,'2024-07-29','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('9ae96280-9cc6-4fa9-b53a-4de4a836de7d',1,'2024-07-28','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('9e4f8f4b-7880-49bf-ad77-960ac1ecb9dd',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','06b917d4-acc5-4533-be59-06ac4fe962fc','4847a140-c664-4959-8aa9-3b1506f202ce'),('9e5d8552-6dcf-49cc-abae-6cf338c3411d',1,'2024-07-30','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('ad4497ba-d25a-453e-8f99-a59d60967062',0,'2024-07-24','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('b708e5af-4652-4f9a-976c-2d0709efd919',0,'2024-07-14','ebca4142-7767-4422-a6e3-09b16d91bdc9','20386020-1073-4dc7-a115-36329149110b','e88f54c9-b1ea-4525-92b8-f2cb46013699'),('b7dfd500-5c4f-48e0-a455-c72b420b155d',0,'2024-07-18','ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','77cf23b5-a974-4c2e-9a9c-bee6fed96749'),('c11cb774-c6f3-463d-880c-cfa09ac10b5b',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','06b917d4-acc5-4533-be59-06ac4fe962fc','58b0a54c-e5ac-45a7-a2a9-7456545c9125'),('c1b9dbbc-a89c-4400-8b0b-57fbc2a2230f',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','88ac11e2-0aa2-4582-a697-5cbe3e8b199c','58b0a54c-e5ac-45a7-a2a9-7456545c9125'),('c7ce15ca-f56e-433f-821d-5ce719b77171',1,'2024-07-27','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('ca44747d-2f2e-490d-8cd3-f1ec3176dd81',0,'2024-07-13','ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','77cf23b5-a974-4c2e-9a9c-bee6fed96749'),('d307e841-3b0a-4f41-92a4-b9458e3f74b7',0,'2024-07-20','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','631e5984-9d03-4569-9a08-c20bafac4df3'),('d8d93cfe-7552-4309-b1d2-32d77d052398',1,'2024-07-23','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('dd103906-bd34-47d6-a389-2ad13fec4a71',0,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','b7c4c1d2-0fd9-42cc-a1c8-028dc7bde79c','58b0a54c-e5ac-45a7-a2a9-7456545c9125'),('e0a7a37c-1f80-4329-926c-ee8051ae2601',0,'2024-07-19','ebca4142-7767-4422-a6e3-09b16d91bdc9','c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','77cf23b5-a974-4c2e-9a9c-bee6fed96749'),('e0f8d247-ea70-4a29-ad88-c6bb3ec57e5c',1,'2024-07-22','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('e1732ae6-8e82-4fdf-9a16-78533ccedac8',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','83cdbcd4-de94-428f-a72d-eb1b42fe8d9c','5e78ee7e-3603-4246-901b-54b2b83c1343'),('e24a7b9c-c541-4b81-84e3-b4cc112a04db',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','73dcc41d-3ff7-487f-83af-d3df5ed2d4f9','c0da6f34-4641-4ed3-8a36-b65ecdc44c0b'),('ea8a6fc2-af3d-46d3-b9ff-425d95129300',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','83cdbcd4-de94-428f-a72d-eb1b42fe8d9c','5e78ee7e-3603-4246-901b-54b2b83c1343'),('ec1222cc-6fb9-4dc6-8469-93570e7c9d82',0,'2024-07-20','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('f4665bda-122a-434f-8a80-8ee09c5daf08',1,'2024-07-26','56f70ac4-e415-484f-99a1-2f76d1b30bc7','d306f39b-e877-41b4-994f-eb82fac44113','c3545acd-841d-4125-92bc-c0cd46ff22af'),('fe9be3a5-ac31-46a2-bf35-8b5032fe83ab',0,'2024-07-18','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915'),('feee90b8-1a14-4aa1-b2c4-2b2d74123fd2',1,'2024-07-25','ebca4142-7767-4422-a6e3-09b16d91bdc9','e6253a31-b770-4028-8d6d-6208bcec9ea9','454feda3-00a1-415c-870c-5b3c918fb915');
/*!40000 ALTER TABLE `dentist_schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dentist_service`
--

DROP TABLE IF EXISTS `dentist_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dentist_service` (
  `dentistid` varchar(255) NOT NULL,
  `serviceid` varchar(255) NOT NULL,
  KEY `FKjjwf005ybemmpyftl60dydq6` (`serviceid`),
  KEY `FKqreh188ksi8puckb1t5jlstnd` (`dentistid`),
  CONSTRAINT `FKjjwf005ybemmpyftl60dydq6` FOREIGN KEY (`serviceid`) REFERENCES `service` (`serviceid`),
  CONSTRAINT `FKqreh188ksi8puckb1t5jlstnd` FOREIGN KEY (`dentistid`) REFERENCES `dentist` (`dentistid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dentist_service`
--

LOCK TABLES `dentist_service` WRITE;
/*!40000 ALTER TABLE `dentist_service` DISABLE KEYS */;
INSERT INTO `dentist_service` VALUES ('e6253a31-b770-4028-8d6d-6208bcec9ea9','9713f384-2876-11ef-9d25-42010a400003'),('c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','9713f323-2876-11ef-9d25-42010a400003'),('c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','9713f356-2876-11ef-9d25-42010a400003'),('c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','9713e2bb-2876-11ef-9d25-42010a400003'),('c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','9713f107-2876-11ef-9d25-42010a400003'),('c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','9713f279-2876-11ef-9d25-42010a400003'),('c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','9713f2ec-2876-11ef-9d25-42010a400003'),('c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','9713f384-2876-11ef-9d25-42010a400003'),('c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','9713f3f2-2876-11ef-9d25-42010a400003'),('20386020-1073-4dc7-a115-36329149110b','9713f279-2876-11ef-9d25-42010a400003'),('20386020-1073-4dc7-a115-36329149110b','9713f2ec-2876-11ef-9d25-42010a400003'),('20386020-1073-4dc7-a115-36329149110b','9713e2bb-2876-11ef-9d25-42010a400003'),('20386020-1073-4dc7-a115-36329149110b','9713f107-2876-11ef-9d25-42010a400003'),('20386020-1073-4dc7-a115-36329149110b','9713f2b6-2876-11ef-9d25-42010a400003'),('93a3d42d-0f2a-4ebb-86b6-9fd8b11116f4','9713e2bb-2876-11ef-9d25-42010a400003'),('93a3d42d-0f2a-4ebb-86b6-9fd8b11116f4','9713f107-2876-11ef-9d25-42010a400003'),('93a3d42d-0f2a-4ebb-86b6-9fd8b11116f4','9713f279-2876-11ef-9d25-42010a400003'),('93a3d42d-0f2a-4ebb-86b6-9fd8b11116f4','9713f2b6-2876-11ef-9d25-42010a400003'),('b7c4c1d2-0fd9-42cc-a1c8-028dc7bde79c','9713f384-2876-11ef-9d25-42010a400003'),('b7c4c1d2-0fd9-42cc-a1c8-028dc7bde79c','9713f356-2876-11ef-9d25-42010a400003'),('b7c4c1d2-0fd9-42cc-a1c8-028dc7bde79c','9713f323-2876-11ef-9d25-42010a400003'),('b7c4c1d2-0fd9-42cc-a1c8-028dc7bde79c','9713f2ec-2876-11ef-9d25-42010a400003'),('b7c4c1d2-0fd9-42cc-a1c8-028dc7bde79c','9713f2b6-2876-11ef-9d25-42010a400003'),('9a6cc0a5-7774-4acd-b6a1-9111140f7421','9713f2b6-2876-11ef-9d25-42010a400003'),('9a6cc0a5-7774-4acd-b6a1-9111140f7421','9713f107-2876-11ef-9d25-42010a400003'),('06b917d4-acc5-4533-be59-06ac4fe962fc','9713f107-2876-11ef-9d25-42010a400003'),('06b917d4-acc5-4533-be59-06ac4fe962fc','9713f2b6-2876-11ef-9d25-42010a400003'),('40ab0249-e04d-44c4-a40b-bc818820f71e','9713f2b6-2876-11ef-9d25-42010a400003'),('4b952cf7-7e24-4b72-8d86-140d2a603c4c','9713f2b6-2876-11ef-9d25-42010a400003'),('73dcc41d-3ff7-487f-83af-d3df5ed2d4f9','9713f2b6-2876-11ef-9d25-42010a400003'),('83cdbcd4-de94-428f-a72d-eb1b42fe8d9c','9713f2b6-2876-11ef-9d25-42010a400003'),('88ac11e2-0aa2-4582-a697-5cbe3e8b199c','9713f2b6-2876-11ef-9d25-42010a400003');
/*!40000 ALTER TABLE `dentist_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dependent`
--

DROP TABLE IF EXISTS `dependent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dependent` (
  `dependentid` varchar(255) NOT NULL,
  `birthday` date DEFAULT NULL,
  `customerid` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dependentid`),
  KEY `FKjhpnvxe78rn3e4nv63kanfy0a` (`customerid`),
  CONSTRAINT `FKjhpnvxe78rn3e4nv63kanfy0a` FOREIGN KEY (`customerid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dependent`
--

LOCK TABLES `dependent` WRITE;
/*!40000 ALTER TABLE `dependent` DISABLE KEYS */;
INSERT INTO `dependent` VALUES ('2f35f3ea-4c0c-4421-86a5-a7fe8828b562','2004-05-02','c713973c-dc0e-4216-8ad5-c689dce39767','phuc'),('64ce203a-300a-4b80-b591-836997b72c53','2024-07-01','aa4f09b5-5bd6-4f7c-a780-28f7ca8ebf2a','sister'),('6a854f07-4d80-498c-9888-f9746a3fad76','2024-07-01','aa4f09b5-5bd6-4f7c-a780-28f7ca8ebf2a','brother'),('90255347-18fe-4e64-a681-053a665b92fa','2020-01-01','f4287374-5092-4023-860b-f8f362e65e69','Nguyễn Thị Nga'),('b5596dc8-8e20-4bb9-a099-b9f964df987c','2020-01-01','aa4f09b5-5bd6-4f7c-a780-28f7ca8ebf2a','mom');
/*!40000 ALTER TABLE `dependent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `notificationid` varchar(255) NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `status` int NOT NULL,
  `dentistid` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `time` time(6) DEFAULT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`notificationid`),
  KEY `FK9j8v2bwy5k782dnlm1k11341k` (`dentistid`),
  CONSTRAINT `FK9j8v2bwy5k782dnlm1k11341k` FOREIGN KEY (`dentistid`) REFERENCES `dentist` (`dentistid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES ('2aa16959-3f79-425e-876c-14911234b17e','day 15-7 i\'ll off',0,'c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','2024-07-12','05:21:38.000000',NULL),('47b03e8b-2152-42f2-a133-a6cb70e24c0b','12',0,'e6253a31-b770-4028-8d6d-6208bcec9ea9','2024-07-18','18:08:10.000000',NULL),('7b5adca8-e9b6-4203-ba1c-9ac8c14b10b0','',0,'e6253a31-b770-4028-8d6d-6208bcec9ea9','2024-07-18','17:07:12.000000',NULL),('da94bf4c-9496-49d9-9c92-a8f2bd46e616','customer thao my need sugery\n',0,'c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','2024-07-12','05:21:15.000000',NULL),('e4106c33-5a42-4919-931d-c2d26ca185fe','Customer Nguyen Van Nam need to have another appointment in day 2024/07/30',0,'e6253a31-b770-4028-8d6d-6208bcec9ea9','2024-07-12','11:16:09.000000',NULL);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_token`
--

DROP TABLE IF EXISTS `password_reset_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_token` (
  `password_reset_tokenid` varchar(255) NOT NULL,
  `expiry_time` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `userid` varchar(255) NOT NULL,
  PRIMARY KEY (`password_reset_tokenid`),
  KEY `FKqv8vfdrfncdyvp6nydsnalmqw` (`userid`),
  CONSTRAINT `FKqv8vfdrfncdyvp6nydsnalmqw` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_token`
--

LOCK TABLES `password_reset_token` WRITE;
/*!40000 ALTER TABLE `password_reset_token` DISABLE KEYS */;
INSERT INTO `password_reset_token` VALUES ('252c02b6-34f8-45cb-a379-7183ddadff81','2024-07-17 20:33:18.745788','aa25d251-6a27-4be4-b505-dbae2a173343','f4287374-5092-4023-860b-f8f362e65e69'),('3349048b-1c06-457f-9705-311717a8cf3c','2024-07-17 20:20:56.614182','b057c0b5-f391-4070-ba54-8864bc28b994','f4287374-5092-4023-860b-f8f362e65e69'),('448fb2ac-3093-41bb-844c-0cf6536851e2','2024-07-17 20:27:07.472617','e4b9d638-0a61-4558-8a0c-0b11591faa4f','f4287374-5092-4023-860b-f8f362e65e69'),('52e1c829-5852-45ca-8bb9-a27533b78b8a','2024-07-17 20:40:42.797571','95c2090e-f59e-495a-a04a-c4519ddb34aa','f4287374-5092-4023-860b-f8f362e65e69'),('71a65a61-1eb5-4d19-b734-9bdf43126533','2024-07-19 21:58:49.766636','76231b0c-22aa-47fa-84fe-49e2445d007e','f4287374-5092-4023-860b-f8f362e65e69'),('8a13dd69-12b5-4186-811a-82cbc1453610','2024-07-17 20:37:46.373895','af12be37-fc96-4092-ac7e-d1b8ea0b3ef9','f4287374-5092-4023-860b-f8f362e65e69'),('8b86c0a8-feeb-4e88-b2c9-30cb832c9e98','2024-07-17 21:09:33.383452','0071df1a-d6bc-471a-811c-6f63cd33a79b','f4287374-5092-4023-860b-f8f362e65e69'),('a2d674a0-e164-482c-aade-18f4612cc6da','2024-07-17 21:38:07.337508','2513e0d9-8205-4586-9dec-097d617ae6f4','9e0ba96c-a598-4d43-9c96-68aaf3f4ac54'),('cfdb0a75-7625-4730-9a63-d42afee8f26c','2024-07-19 21:55:42.598717','8739c2c3-b725-43fd-a605-11068531f131','f4287374-5092-4023-860b-f8f362e65e69'),('e65db2c6-de30-44ff-9b85-098f4324972a','2024-07-18 07:20:05.492106','41d6c5f6-e051-4f76-b319-6bfb6224197f','f4287374-5092-4023-860b-f8f362e65e69'),('eb2606f8-afe9-4ff0-a6f1-b0371e9ab3d2','2024-07-17 20:15:40.087568','b3a774b7-14c1-48d9-98c5-7eca706f1c53','f4287374-5092-4023-860b-f8f362e65e69');
/*!40000 ALTER TABLE `password_reset_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
  `serviceid` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`serviceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` VALUES ('9713e2bb-2876-11ef-9d25-42010a400003','Teeth Cleaning'),('9713f107-2876-11ef-9d25-42010a400003','Tooth Extraction'),('9713f279-2876-11ef-9d25-42010a400003','Cavity Filling'),('9713f2b6-2876-11ef-9d25-42010a400003','Root Canal'),('9713f2ec-2876-11ef-9d25-42010a400003','Dental Checkup'),('9713f323-2876-11ef-9d25-42010a400003','Braces Consultation'),('9713f356-2876-11ef-9d25-42010a400003','Teeth Whitening'),('9713f384-2876-11ef-9d25-42010a400003','Dentures'),('9713f3c4-2876-11ef-9d25-42010a400003','Gum Treatment'),('9713f3f2-2876-11ef-9d25-42010a400003','Dental Implants');
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff` (
  `staffid` varchar(255) NOT NULL,
  `clinicid` varchar(255) NOT NULL,
  PRIMARY KEY (`staffid`),
  KEY `FKc8ynqnlld04hmkyplp2pkbiac` (`clinicid`),
  CONSTRAINT `FK9vsyrx4mylqayfcqds2lutdl1` FOREIGN KEY (`staffid`) REFERENCES `user` (`userid`),
  CONSTRAINT `FKc8ynqnlld04hmkyplp2pkbiac` FOREIGN KEY (`clinicid`) REFERENCES `clinic` (`clinicid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES ('034839f6-eb70-4b0d-a4cd-cb8209c5927c','56f70ac4-e415-484f-99a1-2f76d1b30bc7'),('21f799d6-b102-4792-9e13-030ed7a79502','56f70ac4-e415-484f-99a1-2f76d1b30bc7'),('39aa602f-e467-495c-8d71-0c1d50ac3e29','56f70ac4-e415-484f-99a1-2f76d1b30bc7'),('f51a0823-f36f-4f94-9dfd-e85f5ff2a083','56f70ac4-e415-484f-99a1-2f76d1b30bc7'),('4fb52090-4ef7-494e-be7e-1b8612b98b28','ebca4142-7767-4422-a6e3-09b16d91bdc9'),('6d228f7f-fd2a-4e71-bc69-63b851449747','ebca4142-7767-4422-a6e3-09b16d91bdc9'),('e0434665-35c2-4d28-83ed-91a780b5235e','ebca4142-7767-4422-a6e3-09b16d91bdc9');
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `temporary_user`
--

DROP TABLE IF EXISTS `temporary_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `temporary_user` (
  `userid` varchar(255) NOT NULL,
  `birthday` date DEFAULT NULL,
  `confirmation_token` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` tinyint DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `temporary_user`
--

LOCK TABLES `temporary_user` WRITE;
/*!40000 ALTER TABLE `temporary_user` DISABLE KEYS */;
INSERT INTO `temporary_user` VALUES ('0e0e4915-cccf-4168-9c02-3c59e44ed5ac','2004-09-01','564339e6-16d8-4401-9cca-6b543c56febf',NULL,NULL,'thuvnbse183213+test1@fpt.edu.vn','$2a$10$jHFXqsFU1d8EKVcYvjal7u7DCUGie5n33JGZiNIeysaSyuyHNPHq6','0908989808',0,'Bảo Thư'),('17527603-d85f-436d-be05-3285f9a45533','2001-07-03','2437f9ae-20af-45dd-9bb1-3b1220803034',NULL,NULL,'mommy@gmail.com','$2a$10$d9gCmVE6Uvv4/Q2/h7T6ZOeFdOHyLETSaelU9DMXosrssNr1OU6Ba','0987654322',0,'mommy'),('1e651457-2fcc-48c1-99c8-82475df66eb0','2024-06-21','28e7ff34-ea12-4608-aa42-f18f83d18cc6','K18','K18','phucnhse183026@fpt.edu.vn','$2a$10$4u3QJoRAqYaZyOI4tijsNOW6fddGfX2SWa1sQ0u/FxpCCtkUd5aLG','123456',0,NULL),('21d3a7c6-ae76-4d8f-b904-397fbfd7700f','2024-07-02','de6f2ed6-7803-4f39-a190-3a954fb489c0',NULL,NULL,'a@a.com','$2a$10$LWgqAIdNtX72Ncxm9IyeO.bxc9nRC5QPV7umA1P7GovH1.gdl/lJy','0000000000',0,'a'),('61d9f30c-a482-47fc-8988-4884a510a944','2004-09-01','4600488d-a8ef-4f10-9fca-0eea59136d5b',NULL,NULL,'thuvnbse183213@fpt.edu.vn','$2a$10$u9QBpVsl6izjIj422u4wjeLNxN/ao1cmEo3AiGPoxrAG085vnGTW2','0908989808',0,'Bảo Thư'),('a552d59e-4bda-4ffd-89ba-643c4ce5e6b4','2004-09-01','8f8ae982-07b3-4097-961d-4f836aea24ca',NULL,NULL,'thuvnbse183213@fpt.edu.vn','$2a$10$4dd5yU9RR10fnYatraSrqu7klJw2LDqo85DYcHdbmIojwmiE6inxe','0908989808',0,'Bảo Thư'),('a6d02de7-4078-46e8-8eb1-016e3fa57f2e','2024-07-03','004edf02-04ce-45b4-9eb7-5ac84690ba5b',NULL,NULL,'namvgse183936@fpt.edu.vnn','$2a$10$cZEhuZi8wZKOg8dgxUzEpeI0K6N2bcI1F/M7pbBh.xNzxSfIdLKGy','123',0,'string'),('b2a41576-1f58-468c-81d9-7295f24a35f4','2004-09-01','6771b0ad-ab61-40f6-8548-750ec4a09f44',NULL,NULL,'thuvnbse183213@fpt.edu.vn','$2a$10$m6pdSOe/qsnj52Uj39k56eb05TaSQUvHVb4.aNCkUER5Z3ycHYKZ2','0908989808',0,'Bảo Thư');
/*!40000 ALTER TABLE `temporary_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `time_slot`
--

DROP TABLE IF EXISTS `time_slot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `time_slot` (
  `time_slotid` varchar(255) NOT NULL,
  `start_time` time(6) DEFAULT NULL,
  `clinicid` varchar(255) NOT NULL,
  `slot_number` int NOT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`time_slotid`),
  UNIQUE KEY `unique_time_slot` (`start_time`,`clinicid`,`slot_number`,`date`),
  KEY `FKck78rxds5y8dyqao9h561qv8e` (`clinicid`),
  CONSTRAINT `FKck78rxds5y8dyqao9h561qv8e` FOREIGN KEY (`clinicid`) REFERENCES `clinic` (`clinicid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time_slot`
--

LOCK TABLES `time_slot` WRITE;
/*!40000 ALTER TABLE `time_slot` DISABLE KEYS */;
INSERT INTO `time_slot` VALUES ('b4937f2b-3613-42be-bde6-211b5663f1e3','01:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',1,'2024-07-12'),('c7dc4a0f-2320-4d52-824e-bfb89ba6baa2','01:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',2,'2024-07-12'),('c355c1a3-0f78-40f4-b476-195cfd1c7e08','02:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',3,'2024-07-12'),('59847f41-d40b-4b3d-8dae-9cabd4eabcf7','02:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',4,'2024-07-12'),('41821b06-3477-44a7-a9d5-d85dac034e8f','03:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',5,'2024-07-12'),('9df3e1e4-961b-499e-9f11-6f3819a38190','03:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',6,'2024-07-12'),('0e30f061-1802-4b6b-94cc-b181a8f6f392','06:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',7,'2024-07-12'),('c0cb5785-b2c4-46eb-8483-63d777b25ae8','06:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',1,'2024-07-11'),('319dc35d-4354-4b0b-9152-8f435b4253b3','06:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',8,'2024-07-12'),('08530dbc-8bff-4038-8e6e-e81e8258e31f','06:30:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',2,'2024-07-11'),('bc091ee1-4a7b-491e-a0d4-c98aa16cf883','07:00:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',1,'2024-07-12'),('faf72163-d65f-476d-81de-c1ed08ebaa46','07:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',9,'2024-07-12'),('8c6b85bf-db38-4af5-b57a-742aeaa5fd13','07:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',3,'2024-07-11'),('b5a996e0-e462-4966-a448-4b856a6ed2b2','07:00:00.000000','a7584e4b-6a07-448d-bea6-51ac2107a044',1,'2024-07-12'),('3c4f889b-076d-46d1-8b65-114ffbb5c0cc','07:00:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',1,'2024-07-11'),('d1320097-57c8-42b9-812c-c3c405c97505','07:30:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',2,'2024-07-12'),('36b03470-c2e4-4030-a83c-e0c90a600e82','07:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',10,'2024-07-12'),('6bdcad26-06a4-42c9-85e8-48d8e70d2c7e','07:30:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',4,'2024-07-11'),('77cf23b5-a974-4c2e-9a9c-bee6fed96749','07:30:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',2,'2024-07-11'),('6dcf9018-d9e1-40f1-b99b-f62bfb99c6b1','08:00:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',3,'2024-07-12'),('31f2ae8b-5ce1-4cac-8b7f-ab32c63859d2','08:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',11,'2024-07-12'),('c654bfef-3d91-4b53-92d1-dd162b50618a','08:00:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',1,'2024-07-12'),('1817533f-5867-4539-86f6-4b8936e8116d','08:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',5,'2024-07-11'),('6e8a6ae4-f202-42ff-832e-27a58e7bd5f3','08:00:00.000000','a7584e4b-6a07-448d-bea6-51ac2107a044',2,'2024-07-12'),('e88f54c9-b1ea-4525-92b8-f2cb46013699','08:00:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',3,'2024-07-11'),('38214058-4292-43e8-8ccb-3ed716215d12','08:30:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',4,'2024-07-12'),('d2aa6d9e-024d-4c05-9e1a-d714317b56b9','08:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',12,'2024-07-12'),('930c03a5-7374-434e-91fa-d51c77a10e44','08:30:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',2,'2024-07-12'),('2185d348-982e-49d1-a0bc-c9b87c3d9dd5','08:30:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',6,'2024-07-11'),('631e5984-9d03-4569-9a08-c20bafac4df3','08:30:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',4,'2024-07-11'),('3fe1197c-b497-44d2-b007-7724d7ca9357','09:00:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',5,'2024-07-12'),('0e684c83-d33b-483b-b29c-a90da916cd6c','09:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',13,'2024-07-12'),('58b0a54c-e5ac-45a7-a2a9-7456545c9125','09:00:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',3,'2024-07-12'),('85b69a82-3449-444a-8e83-b10b72a0713f','09:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',7,'2024-07-11'),('9df82dbe-5466-4a7e-ad48-9ca5a4e7ec13','09:00:00.000000','a7584e4b-6a07-448d-bea6-51ac2107a044',3,'2024-07-12'),('454feda3-00a1-415c-870c-5b3c918fb915','09:00:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',5,'2024-07-11'),('9dc87240-e08e-4744-81dd-147e7a63ef2c','09:30:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',6,'2024-07-12'),('46ca8da3-ac29-4801-b25d-5af20ab93e7d','09:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',14,'2024-07-12'),('4847a140-c664-4959-8aa9-3b1506f202ce','09:30:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',4,'2024-07-12'),('1d7b791d-475e-448e-955c-de376e2699f9','09:30:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',8,'2024-07-11'),('402ebc1c-282a-419e-b6d5-4efcd30675b2','09:30:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',6,'2024-07-11'),('0caf1374-7841-4314-b6f3-e7090bb7861c','10:00:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',7,'2024-07-12'),('d6af2563-496d-4a5a-9508-c485b469bd5d','10:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',15,'2024-07-12'),('0ddef3f3-3769-4f0c-a1af-6bc512995f98','10:00:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',5,'2024-07-12'),('7c4d0008-e7de-4f5f-93a8-30d9688bed10','10:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',9,'2024-07-11'),('e8fa0abf-14fa-4543-a213-a45e036343b8','10:00:00.000000','a7584e4b-6a07-448d-bea6-51ac2107a044',4,'2024-07-12'),('768e2049-f853-4c3f-b030-3165ac6389f4','10:00:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',7,'2024-07-11'),('9ed0a5ea-cb0b-408a-9553-846adb1f5445','10:30:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',8,'2024-07-12'),('75e48c5d-9edc-4e9f-8829-a150cdd6b321','10:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',16,'2024-07-12'),('c3545acd-841d-4125-92bc-c0cd46ff22af','10:30:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',6,'2024-07-12'),('754b562b-a06a-4de3-a876-831ff8488eb8','10:30:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',10,'2024-07-11'),('d644f7e0-7a80-49bd-b8b4-70d26379136a','10:30:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',8,'2024-07-11'),('5162994d-ef35-49e1-80c5-ec4f72eba11b','11:00:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',9,'2024-07-12'),('c16f4869-034b-402e-8219-ee7724461cae','11:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',17,'2024-07-12'),('689f565a-8c10-42f7-9d05-4994587a9741','11:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',11,'2024-07-11'),('3df13aca-e0a4-43ce-b020-53a3b4bc23f7','11:00:00.000000','a7584e4b-6a07-448d-bea6-51ac2107a044',5,'2024-07-12'),('bde78d0d-4e6e-4b5e-801a-01d364bc27b1','11:00:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',9,'2024-07-11'),('50f4f7dd-da16-4f28-9a78-6f10ad7eb657','11:30:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',10,'2024-07-12'),('b46e61ec-33cd-4463-a2ad-c0e3532890b6','11:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',18,'2024-07-12'),('30f73e65-39de-48ac-96b0-7980a0c43747','12:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',19,'2024-07-12'),('5e78ee7e-3603-4246-901b-54b2b83c1343','12:00:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',7,'2024-07-12'),('1731173a-1501-490c-a23d-18384bb2d39b','12:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',20,'2024-07-12'),('c0da6f34-4641-4ed3-8a36-b65ecdc44c0b','12:30:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',8,'2024-07-12'),('3c248deb-10b0-47fa-8f2c-0c7287511d00','12:30:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',10,'2024-07-11'),('e2df34c0-a425-4dca-9986-b42cf8e50cb0','13:00:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',11,'2024-07-12'),('ee9e2990-52d7-4986-81e0-638f58dca9f7','13:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',21,'2024-07-12'),('871b580a-3ef0-4075-bbb2-c52e03474fa3','13:00:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',9,'2024-07-12'),('353fe750-5e21-4a76-8b7c-342d6c7c2e76','13:00:00.000000','a7584e4b-6a07-448d-bea6-51ac2107a044',6,'2024-07-12'),('94a5bbc4-4aa6-4865-8134-e4cc21cb9d1d','13:00:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',11,'2024-07-11'),('d8c5e78b-d727-40d1-bb7e-6894d8537a3c','13:30:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',12,'2024-07-12'),('d7392f23-add4-452c-a0f8-ba360e3fcd51','13:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',22,'2024-07-12'),('af1392b6-6968-4193-97fd-f5a9167d7335','13:30:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',10,'2024-07-12'),('d971e303-d15b-44db-a5da-a6a0ea1cbda6','13:30:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',12,'2024-07-11'),('71ae17c9-e37d-4baf-942a-37d10424ab3f','13:30:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',12,'2024-07-11'),('7f47f8a4-54cd-4d83-b833-f4cf74ed3a81','14:00:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',13,'2024-07-12'),('58dd258c-a191-4490-8674-84e080385324','14:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',23,'2024-07-12'),('2112a80e-8a71-4275-bf4d-fd635f5b6415','14:00:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',11,'2024-07-12'),('b84e17f1-1389-46da-af1e-22e2d7a8c82f','14:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',13,'2024-07-11'),('8035f6ff-411f-4fda-a4e6-b83d467884b5','14:00:00.000000','a7584e4b-6a07-448d-bea6-51ac2107a044',7,'2024-07-12'),('1bda9feb-3afd-42ee-aeb4-30c3224609bc','14:00:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',13,'2024-07-11'),('23dbb774-db38-4f6d-8673-45bc37330227','14:30:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',14,'2024-07-12'),('b4c57903-0419-4a9c-a970-8e4b7f6b57db','14:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',24,'2024-07-12'),('15151eec-7821-429b-acbb-6a791fb6ecc4','14:30:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',12,'2024-07-12'),('b993c975-27ac-4229-ae0a-6da09ebd3b96','14:30:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',14,'2024-07-11'),('4bb42ad8-1adf-42f9-9197-7a765305430c','14:30:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',14,'2024-07-11'),('80137f0e-4d42-4953-9b3a-b0fe606f2b2e','15:00:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',15,'2024-07-12'),('096f6abc-e60d-47b6-8b93-8a40552728f2','15:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',25,'2024-07-12'),('e243f34a-ac49-43c8-99d7-032fdfa99f17','15:00:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',13,'2024-07-12'),('c415662a-decd-4245-8a78-237bfade1317','15:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',15,'2024-07-11'),('ea28ce65-0f4d-47c9-81f0-766b3092cb07','15:00:00.000000','a7584e4b-6a07-448d-bea6-51ac2107a044',8,'2024-07-12'),('55af46f1-2adf-43a3-98da-db03f7dc9487','15:00:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',15,'2024-07-11'),('70259164-1c39-41b5-beb4-b48688e5a4ce','15:30:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',16,'2024-07-12'),('f930eabf-14ff-4819-8b96-fdeada9a05e6','15:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',26,'2024-07-12'),('c43bf1ab-5e68-4b76-a071-1fd3341ca058','15:30:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',14,'2024-07-12'),('f81a3889-ab5b-4ee3-9d32-4a2b2c681808','15:30:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',16,'2024-07-11'),('a5f9ce92-e44f-410c-b22a-0be8ef5a2b06','15:30:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',16,'2024-07-11'),('a919c321-14b6-4cb7-a562-d22763c66346','16:00:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',17,'2024-07-12'),('fe7fca6e-5b04-443a-af4b-79a3fa694810','16:00:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',27,'2024-07-12'),('7f94b24f-913a-43d2-a2c7-d3441e6bce89','16:00:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',15,'2024-07-12'),('f3b40f82-4173-4e1c-9c6e-f3437fcbd08a','16:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',17,'2024-07-11'),('5871a3db-b4bf-46ec-9a15-ccb892988b67','16:00:00.000000','a7584e4b-6a07-448d-bea6-51ac2107a044',9,'2024-07-12'),('f6c77015-71cc-4aca-8a38-f1d1268f6242','16:00:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',17,'2024-07-11'),('927a1bf9-e5cd-4a19-b48d-38bda0e0b00e','16:30:00.000000','307f9101-7c0b-4ad1-a0c3-3c43104c2534',18,'2024-07-12'),('88c6f2a5-68e1-41f6-a76d-c058037401e6','16:30:00.000000','52ff9e26-b14a-403b-aabd-e21698c03393',28,'2024-07-12'),('1cd8d4e0-05ab-41bc-b6d6-43db5736070b','16:30:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',16,'2024-07-12'),('1dfdcf22-18ea-4f94-bb6e-bd18c1d64e60','16:30:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',18,'2024-07-11'),('89335d09-f171-4236-9657-e020672677f7','16:30:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',18,'2024-07-11'),('d2bf01ba-f06c-4dce-b675-c7ef696fbcca','17:00:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',17,'2024-07-12'),('4132df36-423b-4269-8924-42d64bafe0bb','17:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',19,'2024-07-11'),('5fe3d09f-cbad-481d-ae39-8d3884610622','17:00:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',19,'2024-07-11'),('693b08c2-eb00-4629-a89c-1731e75868f2','17:30:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',18,'2024-07-12'),('48612003-1e40-46d0-8606-f04a94ea9939','17:30:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',20,'2024-07-11'),('cb8a2c96-8737-4cd7-9f34-2d2e0138684e','17:30:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',20,'2024-07-11'),('35864cdf-b959-470e-b3ad-966d37f26c71','18:00:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',19,'2024-07-12'),('7a15eee4-58a6-4e02-a0c4-bacd80c0bd8b','18:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',21,'2024-07-11'),('561003ee-7377-430a-b6af-b0dd347cb2f7','18:00:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',21,'2024-07-11'),('24f85f4f-1e74-4916-a3ab-fbb921da1fbe','18:30:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',20,'2024-07-12'),('3d61794e-f01d-46c7-a65e-3e384bdaa8fa','18:30:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',22,'2024-07-11'),('ffa9d738-dc5c-467f-8c8d-d6c86bcfc8d6','18:30:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',22,'2024-07-11'),('b01ff734-b783-4f44-8b13-0ee5eef5a886','19:00:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',21,'2024-07-12'),('20437ff6-6964-4454-a3c0-c06e2b73a276','19:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',23,'2024-07-11'),('10307096-ddab-4099-9c82-461f6a2d977e','19:00:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',23,'2024-07-11'),('cc0ef55f-47a4-4e40-b2a3-cba636716117','19:30:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',22,'2024-07-12'),('96baf03c-c723-4bbf-a838-de1d2fa9dc56','19:30:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',24,'2024-07-11'),('a4b4cf82-2b05-4b92-aebe-63eecb4eb4af','19:30:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',24,'2024-07-11'),('ef26ef01-d7c4-4150-a206-4500b895edef','20:00:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',23,'2024-07-12'),('e33b45e8-a4ce-45f4-8e4b-ace89ac64b5a','20:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',25,'2024-07-11'),('4f6c0d3a-9f4f-4dd9-b37c-79d4a1f67913','20:00:00.000000','ebca4142-7767-4422-a6e3-09b16d91bdc9',25,'2024-07-11'),('569e7d8c-c365-40bd-8312-2ef5d12143c2','20:30:00.000000','56f70ac4-e415-484f-99a1-2f76d1b30bc7',24,'2024-07-12'),('3d4453e5-c7f1-4aa6-a41e-f78a190b6712','20:30:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',26,'2024-07-11'),('48cd566e-4a25-4bf6-8429-be82eeda4249','21:00:00.000000','757c247c-6968-43c1-98dd-e1837b0e3b16',27,'2024-07-11');
/*!40000 ALTER TABLE `time_slot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token` (
  `id` varchar(255) NOT NULL,
  `expired` bit(1) NOT NULL,
  `revoked` bit(1) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `token_type` enum('BEARER') DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pddrhgwxnms2aceeku9s2ewy5` (`token`),
  KEY `FKe32ek7ixanakfqsdaokm4q9y2` (`user_id`),
  CONSTRAINT `FKe32ek7ixanakfqsdaokm4q9y2` FOREIGN KEY (`user_id`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
INSERT INTO `token` VALUES ('08b86f5a-0cf9-44a1-935f-3c7f0e92cc76',0x00,0x00,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb24xNTUxOUB0Y2Noby5jb20iLCJpYXQiOjE3MjA3MTQ3NTAsImV4cCI6MTcyMTMxOTU1MH0.20VSIF8Afe1RQYM8R8fzFgR96uLluDGXrQiJTVntFUw','BEARER','4fb52090-4ef7-494e-be7e-1b8612b98b28'),('33049c2a-1c41-4324-a620-6fb142038180',0x00,0x00,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYW5nQGV4YW1wbGUuY29tIiwiaWF0IjoxNzIwNzQ4NTAxLCJleHAiOjE3MjEzNTMzMDF9.y_gmGe6NF9XHtjoq01UI-mguLBFXJAjpMG5MKFwkSmo','BEARER','21f799d6-b102-4792-9e13-030ed7a79502'),('3d4403a4-904a-4281-b298-7a093fb3200b',0x00,0x00,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb3M4NjQxM0BpbGViaS5jb20iLCJpYXQiOjE3MjA3MTUxNjgsImV4cCI6MTcyMTMxOTk2OH0.1EhFLrp2N8OyY7pru41ZVXtQAadhVAJW-Jk5p1r0xJI','BEARER','6d228f7f-fd2a-4e71-bc69-63b851449747'),('40ec4651-97d0-46c6-85a5-e6106ff28a38',0x00,0x00,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYW1AZXhhbXBsZS5jb20iLCJpYXQiOjE3MjA3NTE4NDQsImV4cCI6MTcyMTM1NjY0NH0.sjQ8AXUbFoiTHig0zaxATQ1PeJATNoeN4nd1u1G8yM0','BEARER','034839f6-eb70-4b0d-a4cd-cb8209c5927c'),('95bcb9c5-fdcd-46f6-bb90-a0452ad9ae16',0x00,0x00,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTcyMDc0ODAwMSwiZXhwIjoxNzIxMzUyODAxfQ.X9VGaxLihE9zpgbndjh8yyrjx9MkiCMm0DEYasVsuM4','BEARER','f51a0823-f36f-4f94-9dfd-e85f5ff2a083'),('d6733538-fe91-46ed-bc95-838def7d57d1',0x00,0x00,'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4dWFuQGV4YW1wbGUuY29tIiwiaWF0IjoxNzIwNzUxODkwLCJleHAiOjE3MjEzNTY2OTB9.LzF2eeIJHl2SiyFwAYFCggj2VDMglZSHB8XuAdY4uqY','BEARER','39aa602f-e467-495c-8d71-0c1d50ac3e29');
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userid` varchar(255) NOT NULL,
  `birthday` date DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` enum('GUEST','CUSTOMER','DENTIST','STAFF','MANAGER','ADMIN','BOSS') DEFAULT NULL,
  `status` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('034839f6-eb70-4b0d-a4cd-cb8209c5927c','2002-11-20','nam@example.com','$2a$10$/itfjep6bLUHMBR.d4TXt.Y7yX2P35iXR5hKqSziR.HPXYqk5kMfK','0977662211','STAFF',1,'Nguyễn Thị Nam'),('06b917d4-acc5-4533-be59-06ac4fe962fc','2019-01-01','tran@example.com','$2a$10$cA85by79nuljCjywJnMj8.UVUIEhV6D5ZVPcSB0XCKZ.H1Cot1046','0955662211','DENTIST',1,'Nguyễn Thị Trần'),('0810f6d0-8c3f-4b4c-9de8-11f6bdde68df','2024-07-01','minhtuan030304@gmail.com','$2a$10$3rrw/7ZHB5JD6zGOA0QoTeiGHdBJ4f9HCl8/WhObzqKnfLL21cgsm','0987678901','CUSTOMER',1,'Minh Tuan'),('17cfd57a-2902-4719-b6ea-dfbc722ac422','2024-07-03','quanghuy01062004@gmail.com','$2a$10$3l5U9iB6rSbjSjOFiAQ/uug4qQDVuqW7GKTkGc9T1UMsqUJe772xW','0943241038','CUSTOMER',1,'Nguyen Huu Phuc'),('20386020-1073-4dc7-a115-36329149110b','2008-09-11','yen92830@tccho.com','$2a$10$739Xel5uhHNL1U2ByYzxauA7BLxe2Sz0KStUDKYSNe73XcuT7hsK6','0975575890','DENTIST',1,'Tran Dang Quang'),('21f799d6-b102-4792-9e13-030ed7a79502','1999-03-12','hang@example.com','$2a$10$e/M3SuMLv9OxsJAPH6a7feDyW9a4NdqOmCCgagc8W7gg4j.6QwTUm','0998877331','STAFF',1,'Trần Thị Hằng'),('2f13f8be-167b-4e05-970c-e8c21dde554b','2004-04-11','baothuvoal@gmail.com','$2a$10$75HXK25xuN23kblvOCwvJ.aU46miRf9/eNRvXXWpwpRIERpqnN8H6','0923442841','MANAGER',1,'Man Van Dai'),('39aa602f-e467-495c-8d71-0c1d50ac3e29','2002-06-15','xuan@example.com','$2a$10$ARusTspi2X4HYzZEmptb/OA5cjccXC3N8IR7duEvPwn.Sj4dVzTdO','09988778899','STAFF',1,'Trần Thị Xuân'),('40ab0249-e04d-44c4-a40b-bc818820f71e','2000-02-02','an@example.com','$2a$10$XgDLzkWXoUVoZp7PcAYmzuBY4CVW4pw2u1Uz7xh.nptjFQDvdM.0q','098877668899','DENTIST',1,'Nguyễn Văn An'),('4112862b-f818-4958-90ef-7878d3dd50e3','2024-06-14','tyhqb@mail.com','$2a$10$yW11SFQNUkD8J5hv3mPm5OSyUbHIPe9Z7GE1QJrRf4QLQaghRtDb.','0465738921','ADMIN',1,'Cao Van Toan'),('4b952cf7-7e24-4b72-8d86-140d2a603c4c','2000-02-23','tung@example.com','$2a$10$75A4QAVay4KgNcZj4lPdHelLpsJat1RzlVKhDpRYs.clCpCFzscJO','09573402351','DENTIST',1,'Nguyễn Xuân Tùng'),('4fb52090-4ef7-494e-be7e-1b8612b98b28','2002-07-11','don15519@tccho.com','$2a$10$KKewWf8SGZlRuBKEf.w2wOPrLSunD2UE/cn1EhY5BJ5bNe9KjF/sq','0234567891','STAFF',1,'Tran Le Huy'),('6d228f7f-fd2a-4e71-bc69-63b851449747','2002-01-11','fos86413@ilebi.com','$2a$10$3zlHwi65p5xKQq1zWe53auBZHG7ZlMxYk137W4Fzmv85Lrevltvk.','0892354617','STAFF',1,'Thuy Le Minh'),('73dcc41d-3ff7-487f-83af-d3df5ed2d4f9','2006-12-20','dao@example.com','$2a$10$2Q78uGNcp819Nyr04CkRYOlh8gvRcG07Vclruc0lqJ5a8waiNaQn6','0765432123','DENTIST',1,'Phúc Thị Đào'),('828ac80f-0d5d-43ec-b385-c5da046ebb00','1991-11-04','lkz57781@doolk.com','$2a$10$I1GuY9XhdXNj3lNUV9pi6O3N1aCMGvNxFPCQfRNRPWTf6JRWrZtcy','1298463821','CUSTOMER',1,'Marry Curry'),('83cdbcd4-de94-428f-a72d-eb1b42fe8d9c','1999-04-05','no@example.com','$2a$10$EuG0.QmWJ241YG5ry/./2uyShTD.RxVmHAzdmwfq.oS19lBghQUSa','0997654332','DENTIST',1,'Lan Thị Nở'),('88ac11e2-0aa2-4582-a697-5cbe3e8b199c','2001-03-03','kevin@example.com','$2a$10$NvUs.THFRBHGivzFCn5dzuHGAC.1A284qP4ukKoyzVZ3l8SPPpcEe','0976564548','DENTIST',1,'Kent Phúc'),('93a3d42d-0f2a-4ebb-86b6-9fd8b11116f4','2001-11-01','nui@example.com','$2a$10$t91agfusUwNyZkruZ2QU1ufWjbuUjlEwtvxKmdwY24kB94MbyHueS','0987653212','DENTIST',1,'Lan Thị Núi'),('9a6cc0a5-7774-4acd-b6a1-9111140f7421','2010-03-27','bui@example.com','$2a$10$BKGMq7yE3o25IFnn/7D8jemfBwqoHSvA9EWoZ4xb.59yV6tYG1RGS','0990887766','DENTIST',1,'Trần thị Bùi'),('9e0ba96c-a598-4d43-9c96-68aaf3f4ac54','2004-04-23','kelvinnam.2104@gmail.com','$2a$10$tK7lp72vfis4n6MNzWNBTuI07iAV3wUsWHoTpwMCqJ7xPPbe8Sn6W','0908689680','CUSTOMER',1,'Dai Van Nam'),('aa4f09b5-5bd6-4f7c-a780-28f7ca8ebf2a','2024-06-05','namvgse183936@fpt.edu.vn','$2a$10$apuuIGUHa7x/KCaHwUGba..AaD7XqiHsD1UnWhM5YFph2nn5k4yae','0909080809','CUSTOMER',1,'Nam G'),('ad83ff05-4ab1-4fff-9126-2c7f68971d9f','2004-05-01','bxi57737@ilebi.com','$2a$10$RN5EKcBSOmY1yZLwjpsE7.hK0GV.eKRaX1qHBpwUx/PPbkhk0zj1m','7668653333','MANAGER',1,'Nguyen Huu Phuc'),('ae94dbe0-f16e-4ef6-8bc3-8f4b2b2b81bb','2024-07-08','boss@gmail.com','$2a$10$8ne.LEisET85t3BJxwuc.eSf8J/mWB7IGKPJzYFh8OF57EXNBCcwy','1234987650','BOSS',1,'Hoang Van Hue'),('b7c4c1d2-0fd9-42cc-a1c8-028dc7bde79c','2001-02-07','tuan@example.com','$2a$10$6es1NADXMqcY7nTC41Mte.lWDDf//EKeClsjNhYdmp.ASFI30.Dqu','0909090900','DENTIST',1,'Nguyễn Công Minh Tuấn'),('c713973c-dc0e-4216-8ad5-c689dce39767','2003-08-24','thaomy2408love@gmail.com','$2a$10$6MK7S5HmAdlbxAPx6Z81UO58s26Q/o8qtU52FAWbXw8n8BC.8sraq','1298463822','CUSTOMER',1,'Nguyen Hoang Thao My'),('c85db9b5-9ba9-4d01-b5e3-b8fedf3a5d50','2004-09-11','voz97382@ilebi.com','$2a$10$KXvn7IPLSLHEjYUyzqg64.EB0UB6/Ys5cvqE3aWIWz2EDKcM9Zyuu','0897695758','DENTIST',1,'Nguyen Van An'),('cf866d09-a1ca-4165-a30f-ccf9bb32960b','1997-02-14','example9@mail.com','$2a$10$YVYuKRXWE7izb3unnyBBdOAuHIrIZh.0KSAsuYjHW0ddsxUTbuLly','0992400037','ADMIN',1,'Le Thi Ha'),('d306f39b-e877-41b4-994f-eb82fac44113','2000-02-09','xuan@bli.com','$2a$10$sZ0tlf8cHGeQcqOcDih0oObjxZVPCQohCp6A/wCbLJdMIOUqTiRKO','09908221123','DENTIST',1,'Trần Thị Xuân'),('e0434665-35c2-4d28-83ed-91a780b5235e','2004-06-16','string2','$2a$10$1QRPmu9ynVrmS0Vxda6dKePYTdbc2zIEK6ZlVugQcePHiDWIjnQpq','4413511357','STAFF',1,'Tran Minh Nhat'),('e6253a31-b770-4028-8d6d-6208bcec9ea9','2004-09-01','tjk65890@ilebi.com','$2a$10$or7ECcPl8vPcCkUuvKdw5.zxnbifsVK/5R4jUT9LnRq4j1NwR6cXi','8978675758','DENTIST',1,'Nguyen Thi Nga'),('e74aefdc-f486-4b05-89ee-bb65cd52c613','2024-06-20','nguyenhuuthuc0405@gmail.com','$2a$10$DVyj2GgMDWLkoqArlyifwup39D9Tywoc8v44U8RuOCHY4Kg2k2u/e','0121082112','CUSTOMER',1,'Tran Dang Phuc'),('f4287374-5092-4023-860b-f8f362e65e69','2004-09-02','ncminhtam@gmail.com','$2a$10$OByLYE/njguPtT4NZ3w1eOyQJRRxwbGN2g/abtExpz6EcFyYIUqDa','0987654321','CUSTOMER',1,'Nguyễn Hoàng Nam'),('f51a0823-f36f-4f94-9dfd-e85f5ff2a083','2024-07-01','test@gmail.com','$2a$10$UZ5K/Wf7SsHUH6qoJ49aCue46qv3WjYkVbhsNvYzwK08k8cyVpfV6','0090','STAFF',1,'staff 1'),('f67102ca-3ff1-4bc0-8262-bbc8484d7374','2024-07-02','kelvinnam.21.04@gmail.com','$2a$10$zUEUIRk/KB7xnRr7/6tCduR3vXXG31/hC.FQRY1gbkVizySqYm7q.','0908265992','CUSTOMER',1,'Nguyen Cong Minh Nam'),('fdeb74f7-c20c-4176-8310-37bd1c506d0b','1971-01-22','nhn96552@doolk.com','$2a$10$Zo3THrCzCxCfWrwx6ejcJOegRmixp8r.BevvH9CgXSWQM0ZPEPYge','2398463822','CUSTOMER',1,'Mitter Pottery');
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

-- Dump completed on 2024-07-19 16:47:02
