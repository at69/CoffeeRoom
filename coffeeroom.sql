-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Ven 11 Avril 2014 à 13:42
-- Version du serveur: 5.6.12-log
-- Version de PHP: 5.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `coffeeroom`
--
CREATE DATABASE IF NOT EXISTS `coffeeroom` DEFAULT CHARACTER SET utf8 COLLATE utf8_swedish_ci;
USE `coffeeroom`;

-- --------------------------------------------------------

--
-- Structure de la table `board`
--

CREATE TABLE IF NOT EXISTS `board` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `description` varchar(250) NOT NULL,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `author_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=17 ;

--
-- Contenu de la table `board`
--

INSERT INTO `board` (`id`, `name`, `description`, `creationDate`, `author_id`, `category_id`) VALUES
(1, 'What is Coffee Room', 'Introduction and news about Coffee Room!', '2014-04-11 12:56:55', 1, 1),
(2, 'Introduce yourself', 'Presentation of the new members.', '2014-03-27 09:00:13', 1, 1),
(3, 'Java Virtual Machine', 'Last updates on the JVM.', '2014-03-27 09:05:35', 1, 2),
(4, 'Java SDK', 'Last updates on the SDKs.', '2014-03-27 09:05:35', 1, 2),
(5, 'Servlets', 'All about servlets.', '2014-03-27 09:07:02', 1, 3),
(6, 'JSP', 'All about JavaServer Pages.', '2014-03-27 09:07:02', 1, 3),
(7, 'JPA', 'All about Java Persistence API.', '2014-03-27 09:08:12', 1, 3),
(8, 'Help', 'In case you need help, this is the place.', '2014-03-27 09:08:12', 1, 3),
(9, 'EJB', 'All about Enterprise Java Beans.', '2014-03-29 13:39:36', 1, 4),
(10, 'JMS', 'All about Java Message Service.', '2014-03-27 09:09:28', 1, 4),
(11, 'Web Services', 'How to create Web Services in Java EE.', '2014-03-27 09:10:25', 1, 4),
(12, 'JSF', 'All about JavaServer Faces.', '2014-03-27 09:10:25', 1, 4),
(13, 'Help', 'In case you need help, this is the place.', '2014-03-27 09:10:50', 1, 4);

-- --------------------------------------------------------

--
-- Structure de la table `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `description` varchar(250) NOT NULL,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `author_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Contenu de la table `category`
--

INSERT INTO `category` (`id`, `name`, `description`, `creationDate`, `author_id`) VALUES
(1, 'Presentation', 'Introduction of Coffee Room and its users!', '2014-04-11 12:52:55', 1),
(2, 'News', 'what''s new in the Java world.', '2014-03-27 08:55:43', 1),
(3, 'Java SE', 'All about Java Standard Edition.', '2014-03-29 13:40:02', 1),
(4, 'Java EE', 'All about Java Enterprise Edition.', '2014-03-29 13:40:21', 1);

-- --------------------------------------------------------

--
-- Structure de la table `message`
--

CREATE TABLE IF NOT EXISTS `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `topic_id` int(11) NOT NULL,
  `author_id` int(11) NOT NULL,
  `content` varchar(5000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=85 ;

-- --------------------------------------------------------

--
-- Structure de la table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(200) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Contenu de la table `role`
--

INSERT INTO `role` (`id`, `code`, `description`) VALUES
(1, 'Administrator', 'Has all the rights'),
(2, 'Moderator', 'Can manage topics, messages and view profiles'),
(3, 'User', 'Can display, post and answer topics, view profiles');

-- --------------------------------------------------------

--
-- Structure de la table `topic`
--

CREATE TABLE IF NOT EXISTS `topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isLocked` tinyint(1) NOT NULL DEFAULT '0',
  `author_id` int(11) NOT NULL,
  `board_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=93 ;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lastname` varchar(200) NOT NULL,
  `firstname` varchar(200) NOT NULL,
  `login` varchar(200) NOT NULL,
  `email` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `connectionToken` varchar(200) DEFAULT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

--
-- Contenu de la table `user`
--

INSERT INTO `user` (`id`, `lastname`, `firstname`, `login`, `email`, `password`, `creationDate`, `connectionToken`, `role_id`) VALUES
(1, 'admin', 'admin', 'admin', 'admin@coffeerom.com', 'EDiniUhu/cCiwbWeglXqleFfzjQ=', '2014-04-11 12:50:58', '45e135dd-c148-49d5-8196-cb70ba291592', 1),
(12, 'John', 'Doe', 'jdoe', 'jdoe@gmail.com', 'EDiniUhu/cCiwbWeglXqleFfzjQ=', '2014-04-11 13:03:31', 'f40f5a03-f2c6-409e-a625-703f4b63349c', 2);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
