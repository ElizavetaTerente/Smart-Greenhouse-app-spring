-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: mysql:3306
-- Generation Time: May 19, 2023 at 08:48 AM
-- Server version: 8.0.32
-- PHP Version: 8.1.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db`
--

-- --------------------------------------------------------

--
-- Table structure for table `accesspoint`
--

CREATE TABLE `accesspoint` (
  `accesspoint_id` bigint NOT NULL,
  `accepted` bit(1) NOT NULL,
  `accessible` bit(1) NOT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `location_name` varchar(255) DEFAULT NULL,
  `searching_sensor_station` bit(1) NOT NULL,
  `transmission_interval` time DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  `create_user_username` varchar(255) DEFAULT NULL,
  `update_user_username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `accesspoint`
--

INSERT INTO `accesspoint` (`accesspoint_id`, `accepted`, `accessible`, `create_date`, `enabled`, `location_name`, `searching_sensor_station`, `transmission_interval`, `update_date`, `create_user_username`, `update_user_username`) VALUES
(1, b'0', b'1', NULL, b'1', 'west wing', b'0', '00:00:05', NULL, NULL, NULL),
(2, b'1', b'0', '2023-05-18 11:36:54.036217', b'1', 'Daniels Zimmer', b'0', '00:10:59', '2023-05-19 07:39:19.529709', 'amir', 'daniel'),
(3, b'0', b'0', NULL, b'1', 'north wing', b'0', '00:00:15', NULL, NULL, NULL),
(5, b'1', b'0', '2023-05-18 16:57:31.140470', b'1', 'Daniels Zimmer', b'0', '00:05:00', '2023-05-18 21:02:06.018481', 'amir', 'amir');

-- --------------------------------------------------------

--
-- Table structure for table `audit_event`
--

CREATE TABLE `audit_event` (
  `log_id` bigint NOT NULL,
  `corresponding_object` varchar(255) NOT NULL,
  `event_type` varchar(255) NOT NULL,
  `timestamp` datetime(6) NOT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `managed_stations`
--

CREATE TABLE `managed_stations` (
  `sensor_station_id` bigint NOT NULL,
  `userx_username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `managed_stations`
--

INSERT INTO `managed_stations` (`sensor_station_id`, `userx_username`) VALUES
(1, 'daniel');

-- --------------------------------------------------------

--
-- Table structure for table `observed_stations`
--

CREATE TABLE `observed_stations` (
  `sensor_station_id` bigint NOT NULL,
  `userx_username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sensor_data`
--

CREATE TABLE `sensor_data` (
  `sensor_data_id` bigint NOT NULL,
  `data_time` datetime(6) DEFAULT NULL,
  `data_value` double NOT NULL,
  `sensor_type` smallint DEFAULT NULL,
  `sensorstation_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sensor_data`
--

INSERT INTO `sensor_data` (`sensor_data_id`, `data_time`, `data_value`, `sensor_type`, `sensorstation_id`) VALUES
(1, '2023-05-19 08:52:08.000000', 47.44, 0, 1),
(2, '2023-05-19 08:52:08.000000', 51.65, 1, 1),
(3, '2023-05-19 08:52:08.000000', 85.24, 2, 1),
(4, '2023-05-19 08:52:08.000000', 19.03, 3, 1),
(5, '2023-05-19 08:52:08.000000', 39.51, 4, 1),
(6, '2023-05-19 08:52:08.000000', 46.59, 5, 1),
(7, '2023-05-19 08:52:08.000000', 31.71, 0, 2),
(8, '2023-05-19 08:52:08.000000', 36.66, 1, 2),
(9, '2023-05-19 08:52:08.000000', 44.73, 2, 2),
(10, '2023-05-19 08:52:08.000000', 92.65, 3, 2),
(11, '2023-05-19 08:52:08.000000', 22.48, 4, 2),
(12, '2023-05-19 08:52:08.000000', 25.02, 5, 2),
(13, '2023-05-19 08:52:38.000000', 83.44, 0, 1),
(14, '2023-05-19 08:52:38.000000', 80.39, 1, 1),
(15, '2023-05-19 08:52:38.000000', 27.75, 2, 1),
(16, '2023-05-19 08:52:38.000000', 33.7, 3, 1),
(17, '2023-05-19 08:52:38.000000', 88.74, 4, 1),
(18, '2023-05-19 08:52:38.000000', 26.19, 5, 1),
(19, '2023-05-19 08:52:38.000000', 7.23, 0, 2),
(20, '2023-05-19 08:52:38.000000', 33.14, 1, 2),
(21, '2023-05-19 08:52:38.000000', 63.56, 2, 2),
(22, '2023-05-19 08:52:38.000000', 5.57, 3, 2),
(23, '2023-05-19 08:52:38.000000', 6.83, 4, 2),
(24, '2023-05-19 08:52:38.000000', 60.3, 5, 2),
(25, '2023-05-19 08:53:08.000000', 67.16, 0, 1),
(26, '2023-05-19 08:53:08.000000', 85.48, 1, 1),
(27, '2023-05-19 08:53:08.000000', 60.82, 2, 1),
(28, '2023-05-19 08:53:08.000000', 0.08, 3, 1),
(29, '2023-05-19 08:53:08.000000', 69.08, 4, 1),
(30, '2023-05-19 08:53:08.000000', 93.77, 5, 1),
(31, '2023-05-19 08:53:08.000000', 45.12, 0, 2),
(32, '2023-05-19 08:53:08.000000', 8.78, 1, 2),
(33, '2023-05-19 08:53:08.000000', 11.83, 2, 2),
(34, '2023-05-19 08:53:08.000000', 64.93, 3, 2),
(35, '2023-05-19 08:53:08.000000', 56.48, 4, 2),
(36, '2023-05-19 08:53:08.000000', 4.07, 5, 2),
(37, '2023-05-19 08:53:38.000000', 3.62, 0, 1),
(38, '2023-05-19 08:53:38.000000', 59.16, 1, 1),
(39, '2023-05-19 08:53:38.000000', 31.43, 2, 1),
(40, '2023-05-19 08:53:38.000000', 47.25, 3, 1),
(41, '2023-05-19 08:53:38.000000', 51.44, 4, 1),
(42, '2023-05-19 08:53:38.000000', 21.39, 5, 1),
(43, '2023-05-19 08:53:38.000000', 12.33, 0, 2),
(44, '2023-05-19 08:53:38.000000', 53.66, 1, 2),
(45, '2023-05-19 08:53:38.000000', 44.05, 2, 2),
(46, '2023-05-19 08:53:38.000000', 57.46, 3, 2),
(47, '2023-05-19 08:53:38.000000', 72.43, 4, 2),
(48, '2023-05-19 08:53:38.000000', 50.79, 5, 2),
(49, '2023-05-19 08:54:08.000000', 56.15, 0, 1),
(50, '2023-05-19 08:54:08.000000', 35.72, 1, 1),
(51, '2023-05-19 08:54:08.000000', 25.38, 2, 1),
(52, '2023-05-19 08:54:08.000000', 44.49, 3, 1),
(53, '2023-05-19 08:54:08.000000', 86.37, 4, 1),
(54, '2023-05-19 08:54:08.000000', 26.7, 5, 1),
(55, '2023-05-19 08:54:08.000000', 18.46, 0, 2),
(56, '2023-05-19 08:54:08.000000', 11.38, 1, 2),
(57, '2023-05-19 08:54:08.000000', 23.43, 2, 2),
(58, '2023-05-19 08:54:08.000000', 55.25, 3, 2),
(59, '2023-05-19 08:54:08.000000', 26.96, 4, 2),
(60, '2023-05-19 08:54:08.000000', 49.7, 5, 2),
(61, '2023-05-19 08:54:38.000000', 20.32, 0, 1),
(62, '2023-05-19 08:54:38.000000', 89.34, 1, 1),
(63, '2023-05-19 08:54:38.000000', 34.11, 2, 1),
(64, '2023-05-19 08:54:38.000000', 20.84, 3, 1),
(65, '2023-05-19 08:54:38.000000', 80.93, 4, 1),
(66, '2023-05-19 08:54:38.000000', 12.65, 5, 1),
(67, '2023-05-19 08:54:38.000000', 77.69, 0, 2),
(68, '2023-05-19 08:54:38.000000', 32.65, 1, 2),
(69, '2023-05-19 08:54:38.000000', 14.35, 2, 2),
(70, '2023-05-19 08:54:38.000000', 59.54, 3, 2),
(71, '2023-05-19 08:54:38.000000', 72.87, 4, 2),
(72, '2023-05-19 08:54:38.000000', 25.17, 5, 2),
(73, '2023-05-19 08:55:08.000000', 93.52, 0, 1),
(74, '2023-05-19 08:55:08.000000', 2.96, 1, 1),
(75, '2023-05-19 08:55:08.000000', 83.27, 2, 1),
(76, '2023-05-19 08:55:08.000000', 13, 3, 1),
(77, '2023-05-19 08:55:08.000000', 28.81, 4, 1),
(78, '2023-05-19 08:55:08.000000', 33.08, 5, 1),
(79, '2023-05-19 08:55:08.000000', 37.04, 0, 2),
(80, '2023-05-19 08:55:08.000000', 24.68, 1, 2),
(81, '2023-05-19 08:55:08.000000', 56.18, 2, 2),
(82, '2023-05-19 08:55:08.000000', 42.58, 3, 2),
(83, '2023-05-19 08:55:08.000000', 39.84, 4, 2),
(84, '2023-05-19 08:55:08.000000', 42.73, 5, 2),
(85, '2023-05-19 08:55:38.000000', 32.82, 0, 1),
(86, '2023-05-19 08:55:38.000000', 9.55, 1, 1),
(87, '2023-05-19 08:55:38.000000', 62.92, 2, 1),
(88, '2023-05-19 08:55:38.000000', 3.13, 3, 1),
(89, '2023-05-19 08:55:38.000000', 14.89, 4, 1),
(90, '2023-05-19 08:55:38.000000', 81.11, 5, 1),
(91, '2023-05-19 08:55:38.000000', 10.3, 0, 2),
(92, '2023-05-19 08:55:38.000000', 69.3, 1, 2),
(93, '2023-05-19 08:55:38.000000', 80.65, 2, 2),
(94, '2023-05-19 08:55:38.000000', 48.36, 3, 2),
(95, '2023-05-19 08:55:38.000000', 8.1, 4, 2),
(96, '2023-05-19 08:55:38.000000', 73.01, 5, 2),
(97, '2023-05-19 08:56:08.000000', 32.85, 0, 1),
(98, '2023-05-19 08:56:08.000000', 98.22, 1, 1),
(99, '2023-05-19 08:56:08.000000', 51.97, 2, 1),
(100, '2023-05-19 08:56:08.000000', 98.5, 3, 1),
(101, '2023-05-19 08:56:08.000000', 51.41, 4, 1),
(102, '2023-05-19 08:56:08.000000', 63.04, 5, 1),
(103, '2023-05-19 08:56:08.000000', 7.63, 0, 2),
(104, '2023-05-19 08:56:08.000000', 44.64, 1, 2),
(105, '2023-05-19 08:56:08.000000', 37.35, 2, 2),
(106, '2023-05-19 08:56:08.000000', 28.44, 3, 2),
(107, '2023-05-19 08:56:08.000000', 3.82, 4, 2),
(108, '2023-05-19 08:56:08.000000', 46.15, 5, 2),
(109, '2023-05-19 08:56:38.000000', 84.34, 0, 1),
(110, '2023-05-19 08:56:38.000000', 67.08, 1, 1),
(111, '2023-05-19 08:56:38.000000', 42.84, 2, 1),
(112, '2023-05-19 08:56:38.000000', 44.5, 3, 1),
(113, '2023-05-19 08:56:38.000000', 15.4, 4, 1),
(114, '2023-05-19 08:56:38.000000', 11.86, 5, 1),
(115, '2023-05-19 08:56:38.000000', 27.52, 0, 2),
(116, '2023-05-19 08:56:38.000000', 73.34, 1, 2),
(117, '2023-05-19 08:56:38.000000', 97.58, 2, 2),
(118, '2023-05-19 08:56:38.000000', 9.74, 3, 2),
(119, '2023-05-19 08:56:38.000000', 2.54, 4, 2),
(120, '2023-05-19 08:56:38.000000', 38.01, 5, 2),
(121, '2023-05-19 08:57:08.000000', 37.12, 0, 1),
(122, '2023-05-19 08:57:08.000000', 21.16, 1, 1),
(123, '2023-05-19 08:57:08.000000', 70.64, 2, 1),
(124, '2023-05-19 08:57:08.000000', 50.84, 3, 1),
(125, '2023-05-19 08:57:08.000000', 44.14, 4, 1),
(126, '2023-05-19 08:57:08.000000', 89.18, 5, 1),
(127, '2023-05-19 08:57:08.000000', 14.14, 0, 2),
(128, '2023-05-19 08:57:08.000000', 8.62, 1, 2),
(129, '2023-05-19 08:57:08.000000', 41.35, 2, 2),
(130, '2023-05-19 08:57:08.000000', 60.43, 3, 2),
(131, '2023-05-19 08:57:08.000000', 12.42, 4, 2),
(132, '2023-05-19 08:57:08.000000', 55.3, 5, 2),
(133, '2023-05-19 08:57:38.000000', 77.93, 0, 1),
(134, '2023-05-19 08:57:38.000000', 32.89, 1, 1),
(135, '2023-05-19 08:57:38.000000', 22.08, 2, 1),
(136, '2023-05-19 08:57:38.000000', 66.28, 3, 1),
(137, '2023-05-19 08:57:38.000000', 11.66, 4, 1),
(138, '2023-05-19 08:57:38.000000', 98.75, 5, 1),
(139, '2023-05-19 08:57:38.000000', 53.75, 0, 2),
(140, '2023-05-19 08:57:38.000000', 27.63, 1, 2),
(141, '2023-05-19 08:57:38.000000', 21.92, 2, 2),
(142, '2023-05-19 08:57:38.000000', 51.45, 3, 2),
(143, '2023-05-19 08:57:38.000000', 69.41, 4, 2),
(144, '2023-05-19 08:57:38.000000', 76.22, 5, 2),
(145, '2023-05-19 08:58:08.000000', 38.96, 0, 1),
(146, '2023-05-19 08:58:08.000000', 96.99, 1, 1),
(147, '2023-05-19 08:58:08.000000', 5.9, 2, 1),
(148, '2023-05-19 08:58:08.000000', 74.63, 3, 1),
(149, '2023-05-19 08:58:08.000000', 51.43, 4, 1),
(150, '2023-05-19 08:58:08.000000', 6.23, 5, 1),
(151, '2023-05-19 08:58:08.000000', 36.9, 0, 2);

-- --------------------------------------------------------

--
-- Table structure for table `sensor_limit`
--

CREATE TABLE `sensor_limit` (
  `sensor_limit_id` bigint NOT NULL,
  `overrun_interval` decimal(21,0) DEFAULT NULL,
  `sensor_type` smallint DEFAULT NULL,
  `threshold_max` double NOT NULL,
  `threshold_min` double NOT NULL,
  `sensorstation_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sensor_limit`
--

INSERT INTO `sensor_limit` (`sensor_limit_id`, `overrun_interval`, `sensor_type`, `threshold_max`, `threshold_min`, `sensorstation_id`) VALUES
(1, 600000000000, 0, 50, 30, 1),
(2, 600000000000, 1, 30, 10, 1),
(3, 600000000000, 2, 60, 30, 1),
(4, 600000000000, 3, 50, 40, 1),
(5, 600000000000, 4, 60, 30, 1),
(6, 600000000000, 5, 20, 0, 1),
(7, 600000000000, 0, 0, 0, 2),
(8, 600000000000, 1, 0, 0, 2),
(9, 600000000000, 2, 0, 0, 2),
(10, 600000000000, 3, 0, 0, 2),
(11, 600000000000, 4, 0, 0, 2),
(12, 600000000000, 5, 0, 0, 2),
(13, 600000000000, 0, 0, 0, 3),
(14, 600000000000, 1, 0, 0, 3),
(15, 600000000000, 2, 0, 0, 3),
(16, 600000000000, 3, 0, 0, 3),
(17, 600000000000, 4, 0, 0, 3),
(18, 600000000000, 5, 0, 0, 3);

-- --------------------------------------------------------

--
-- Table structure for table `sensor_station`
--

CREATE TABLE `sensor_station` (
  `sensor_station_id` bigint NOT NULL,
  `accepted` bit(1) NOT NULL,
  `accessible` bit(1) NOT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `sensor_station_dip_id` bigint DEFAULT NULL,
  `sensor_station_name` varchar(255) DEFAULT NULL,
  `universally_unique_identifier` varchar(255) DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  `accesspoint_id` bigint DEFAULT NULL,
  `create_user_username` varchar(255) DEFAULT NULL,
  `update_user_username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sensor_station`
--

INSERT INTO `sensor_station` (`sensor_station_id`, `accepted`, `accessible`, `create_date`, `enabled`, `sensor_station_dip_id`, `sensor_station_name`, `universally_unique_identifier`, `update_date`, `accesspoint_id`, `create_user_username`, `update_user_username`) VALUES
(1, b'1', b'1', '2023-05-18 11:38:34.593927', b'1', 1, NULL, NULL, '2023-05-19 10:48:14.095219', 2, 'amir', 'admin'),
(2, b'1', b'1', '2023-05-18 11:38:48.969344', b'1', 72, NULL, NULL, '2023-05-19 09:05:29.826508', 2, 'amir', 'admin'),
(3, b'1', b'1', '2023-05-18 12:08:17.461492', b'1', 47, NULL, NULL, '2023-05-18 21:08:43.662462', NULL, 'amir', 'amir');

-- --------------------------------------------------------

--
-- Table structure for table `station_image`
--

CREATE TABLE `station_image` (
  `image_id` bigint NOT NULL,
  `picture_path` varchar(255) DEFAULT NULL,
  `upload_date` datetime(6) DEFAULT NULL,
  `sensorstation_id` bigint DEFAULT NULL,
  `enabled` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `userx`
--

CREATE TABLE `userx` (
  `username` varchar(255) NOT NULL,
  `create_date` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `update_date` datetime(6) DEFAULT NULL,
  `create_user_username` varchar(255) DEFAULT NULL,
  `update_user_username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `userx`
--

INSERT INTO `userx` (`username`, `create_date`, `email`, `enabled`, `firstname`, `lastname`, `password`, `phone`, `update_date`, `create_user_username`, `update_user_username`) VALUES
('admin', '2023-04-01 18:19:26.492745', 'name@google.com', b'1', 'Name', 'Surname', '$2a$10$DWHhsRtjYX33SSKvNcQt0.fKatbz90J4wPc7w2w0S7h5q2chnA3.2', '+436706666666', NULL, NULL, NULL),
('amir', '2023-04-01 18:19:26.492745', 'amir@google.com', b'1', 'Amir', 'Dzelalagic', '$2a$10$DWHhsRtjYX33SSKvNcQt0.fKatbz90J4wPc7w2w0S7h5q2chnA3.2', '+436701111111', NULL, NULL, NULL),
('daniel', '2023-04-01 18:19:26.492745', 'daniel@google.com', b'1', 'Daniel', 'Stoffler', '$2a$10$DWHhsRtjYX33SSKvNcQt0.fKatbz90J4wPc7w2w0S7h5q2chnA3.2', '+436702222222', NULL, NULL, NULL),
('elizaveta', '2023-04-01 18:19:26.492745', 'eli@google.com', b'1', 'Elizaveta', 'Terente', '$2a$10$DWHhsRtjYX33SSKvNcQt0.fKatbz90J4wPc7w2w0S7h5q2chnA3.2', '+436703333333', NULL, NULL, NULL),
('kristina', '2023-04-01 18:19:26.492745', 'kris@google.com', b'1', 'Kristina', 'Alexandrova', '$2a$10$DWHhsRtjYX33SSKvNcQt0.fKatbz90J4wPc7w2w0S7h5q2chnA3.2', '+436704444444', NULL, NULL, NULL),
('lukas', '2023-04-01 18:19:26.492745', 'lukas@google.com', b'1', 'Lukas', 'Geisler', '$2a$10$DWHhsRtjYX33SSKvNcQt0.fKatbz90J4wPc7w2w0S7h5q2chnA3.2', '+436705555555', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `userx_user_role`
--

CREATE TABLE `userx_user_role` (
  `userx_username` varchar(255) NOT NULL,
  `roles` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `userx_user_role`
--

INSERT INTO `userx_user_role` (`userx_username`, `roles`) VALUES
('admin', 'ADMIN'),
('amir', 'ADMIN'),
('daniel', 'GARDENER'),
('elizaveta', 'USER'),
('kristina', 'GARDENER'),
('lukas', 'USER');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accesspoint`
--
ALTER TABLE `accesspoint`
  ADD PRIMARY KEY (`accesspoint_id`),
  ADD KEY `FK8etsauqcn3h2gqorb783e07pp` (`create_user_username`),
  ADD KEY `FKpvb1pm5ggsistq1lmqjjasc1t` (`update_user_username`);

--
-- Indexes for table `audit_event`
--
ALTER TABLE `audit_event`
  ADD PRIMARY KEY (`log_id`);

--
-- Indexes for table `managed_stations`
--
ALTER TABLE `managed_stations`
  ADD PRIMARY KEY (`sensor_station_id`,`userx_username`),
  ADD KEY `FKc5h4x57kk1xwosa1jck2lu37h` (`userx_username`);

--
-- Indexes for table `observed_stations`
--
ALTER TABLE `observed_stations`
  ADD PRIMARY KEY (`sensor_station_id`,`userx_username`),
  ADD KEY `FKkg2v3c6j8v5fkw3iek8gv7547` (`userx_username`);

--
-- Indexes for table `sensor_data`
--
ALTER TABLE `sensor_data`
  ADD PRIMARY KEY (`sensor_data_id`),
  ADD KEY `FKr1e3y9jt3giqhp1xnvf8akwfv` (`sensorstation_id`);

--
-- Indexes for table `sensor_limit`
--
ALTER TABLE `sensor_limit`
  ADD PRIMARY KEY (`sensor_limit_id`),
  ADD KEY `FKkicssphlh63afrs0gj1ojedbn` (`sensorstation_id`);

--
-- Indexes for table `sensor_station`
--
ALTER TABLE `sensor_station`
  ADD PRIMARY KEY (`sensor_station_id`),
  ADD KEY `FK4a84qees7eovgndb33vqsxynh` (`accesspoint_id`),
  ADD KEY `FKfcxllj4kncu7retf6epg92f2d` (`create_user_username`),
  ADD KEY `FKa7uw4lg2qbpidduiihl87uy6l` (`update_user_username`);

--
-- Indexes for table `station_image`
--
ALTER TABLE `station_image`
  ADD PRIMARY KEY (`image_id`),
  ADD KEY `FK2hnjn4hp87pk5ccnpe63s1gyq` (`sensorstation_id`);

--
-- Indexes for table `userx`
--
ALTER TABLE `userx`
  ADD PRIMARY KEY (`username`),
  ADD KEY `FKisy78ou3a9gplc70qko4k12gp` (`create_user_username`),
  ADD KEY `FKsim9cogkheo426iaatjt6b52n` (`update_user_username`);

--
-- Indexes for table `userx_user_role`
--
ALTER TABLE `userx_user_role`
  ADD UNIQUE KEY `Username_Role` (`userx_username`,`roles`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accesspoint`
--
ALTER TABLE `accesspoint`
  MODIFY `accesspoint_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `audit_event`
--
ALTER TABLE `audit_event`
  MODIFY `log_id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `sensor_data`
--
ALTER TABLE `sensor_data`
  MODIFY `sensor_data_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2785;

--
-- AUTO_INCREMENT for table `sensor_limit`
--
ALTER TABLE `sensor_limit`
  MODIFY `sensor_limit_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `sensor_station`
--
ALTER TABLE `sensor_station`
  MODIFY `sensor_station_id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `station_image`
--
ALTER TABLE `station_image`
  MODIFY `image_id` bigint NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `accesspoint`
--
ALTER TABLE `accesspoint`
  ADD CONSTRAINT `FK8etsauqcn3h2gqorb783e07pp` FOREIGN KEY (`create_user_username`) REFERENCES `userx` (`username`),
  ADD CONSTRAINT `FKpvb1pm5ggsistq1lmqjjasc1t` FOREIGN KEY (`update_user_username`) REFERENCES `userx` (`username`);

--
-- Constraints for table `managed_stations`
--
ALTER TABLE `managed_stations`
  ADD CONSTRAINT `FKc5h4x57kk1xwosa1jck2lu37h` FOREIGN KEY (`userx_username`) REFERENCES `userx` (`username`),
  ADD CONSTRAINT `FKrjnwu9p324lehkys650hnbw3x` FOREIGN KEY (`sensor_station_id`) REFERENCES `sensor_station` (`sensor_station_id`);

--
-- Constraints for table `observed_stations`
--
ALTER TABLE `observed_stations`
  ADD CONSTRAINT `FKkg2v3c6j8v5fkw3iek8gv7547` FOREIGN KEY (`userx_username`) REFERENCES `userx` (`username`),
  ADD CONSTRAINT `FKoebi05am76s7xgihoxxgylfm8` FOREIGN KEY (`sensor_station_id`) REFERENCES `sensor_station` (`sensor_station_id`);

--
-- Constraints for table `sensor_data`
--
ALTER TABLE `sensor_data`
  ADD CONSTRAINT `FKr1e3y9jt3giqhp1xnvf8akwfv` FOREIGN KEY (`sensorstation_id`) REFERENCES `sensor_station` (`sensor_station_id`);

--
-- Constraints for table `sensor_limit`
--
ALTER TABLE `sensor_limit`
  ADD CONSTRAINT `FKkicssphlh63afrs0gj1ojedbn` FOREIGN KEY (`sensorstation_id`) REFERENCES `sensor_station` (`sensor_station_id`);

--
-- Constraints for table `sensor_station`
--
ALTER TABLE `sensor_station`
  ADD CONSTRAINT `FK4a84qees7eovgndb33vqsxynh` FOREIGN KEY (`accesspoint_id`) REFERENCES `accesspoint` (`accesspoint_id`),
  ADD CONSTRAINT `FKa7uw4lg2qbpidduiihl87uy6l` FOREIGN KEY (`update_user_username`) REFERENCES `userx` (`username`),
  ADD CONSTRAINT `FKfcxllj4kncu7retf6epg92f2d` FOREIGN KEY (`create_user_username`) REFERENCES `userx` (`username`);

--
-- Constraints for table `station_image`
--
ALTER TABLE `station_image`
  ADD CONSTRAINT `FK2hnjn4hp87pk5ccnpe63s1gyq` FOREIGN KEY (`sensorstation_id`) REFERENCES `sensor_station` (`sensor_station_id`);

--
-- Constraints for table `userx`
--
ALTER TABLE `userx`
  ADD CONSTRAINT `FKisy78ou3a9gplc70qko4k12gp` FOREIGN KEY (`create_user_username`) REFERENCES `userx` (`username`),
  ADD CONSTRAINT `FKsim9cogkheo426iaatjt6b52n` FOREIGN KEY (`update_user_username`) REFERENCES `userx` (`username`);

--
-- Constraints for table `userx_user_role`
--
ALTER TABLE `userx_user_role`
  ADD CONSTRAINT `FKrjj2u88612lxcux0bub6roal9` FOREIGN KEY (`userx_username`) REFERENCES `userx` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
