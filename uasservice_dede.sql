-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 19, 2023 at 07:11 AM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `uasservice_dede`
--

-- --------------------------------------------------------

--
-- Table structure for table `tlayanan_dede`
--

CREATE TABLE `tlayanan_dede` (
  `id_layanan` varchar(20) NOT NULL,
  `nama_layanan` varchar(50) NOT NULL,
  `harga_layanan` int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tlayanan_dede`
--

INSERT INTO `tlayanan_dede` (`id_layanan`, `nama_layanan`, `harga_layanan`) VALUES
('LY001', 'Ganti Oli', 65000),
('LY002', 'Service Rutin', 75000),
('LY003', 'Ganti Ban', 150000);

-- --------------------------------------------------------

--
-- Table structure for table `tmekanik_dede`
--

CREATE TABLE `tmekanik_dede` (
  `id_mekanik` varchar(10) NOT NULL,
  `nama_mekanik` varchar(50) NOT NULL,
  `alamat_mekanik` varchar(30) NOT NULL,
  `telepon_mekanik` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tmekanik_dede`
--

INSERT INTO `tmekanik_dede` (`id_mekanik`, `nama_mekanik`, `alamat_mekanik`, `telepon_mekanik`) VALUES
('MK001', 'Jhon', 'Bantar', '0823444444'),
('MK003', 'Agus', 'Cibaduyut', '0823444444'),
('MK004', 'Ibnu', 'Cisayong', '087236512673');

-- --------------------------------------------------------

--
-- Table structure for table `tpelanggan_dede`
--

CREATE TABLE `tpelanggan_dede` (
  `id_pelanggan` varchar(20) NOT NULL,
  `nama_pelanggan` varchar(50) NOT NULL,
  `alamat_pelanggan` varchar(30) NOT NULL,
  `telepon_pelanggan` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tpelanggan_dede`
--

INSERT INTO `tpelanggan_dede` (`id_pelanggan`, `nama_pelanggan`, `alamat_pelanggan`, `telepon_pelanggan`) VALUES
('002', 'Risman', 'Ciawi', '08762172'),
('003', 'Umar', 'Tasik', '08762172'),
('004', 'Dede', 'Bandung', '086251562131');

-- --------------------------------------------------------

--
-- Table structure for table `ttransaksi_dede`
--

CREATE TABLE `ttransaksi_dede` (
  `idTransaksi` varchar(20) NOT NULL,
  `tanggalTransaksi` date NOT NULL,
  `idPelanggan` varchar(30) NOT NULL,
  `idLayanan` varchar(30) NOT NULL,
  `totalBiaya` varchar(15) NOT NULL,
  `idMekanik` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ttransaksi_dede`
--

INSERT INTO `ttransaksi_dede` (`idTransaksi`, `tanggalTransaksi`, `idPelanggan`, `idLayanan`, `totalBiaya`, `idMekanik`) VALUES
('003', '2023-06-06', 'Dede', 'Ganti Oli', '65000', 'Ibnu'),
('002', '2023-05-31', 'Risman', 'Service Rutin', '75000', 'Jhon'),
('005', '2023-06-06', 'umar', 'Ganti Ban', '75000', 'Agus');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
