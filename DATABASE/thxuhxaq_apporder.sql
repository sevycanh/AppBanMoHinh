-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Apr 16, 2024 at 10:32 PM
-- Server version: 5.7.41-cll-lve
-- PHP Version: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `thxuhxaq_apporder`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_account`
--

CREATE TABLE `tbl_account` (
  `account_id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `address` text NOT NULL,
  `province` text NOT NULL,
  `district` text NOT NULL,
  `ward` text NOT NULL,
  `administrative_address` text NOT NULL,
  `phone` varchar(10) NOT NULL,
  `coin` int(11) NOT NULL,
  `check_in` int(1) NOT NULL,
  `luckybox` int(1) NOT NULL,
  `role` int(1) NOT NULL COMMENT '1-User, 0-Admin',
  `status` int(1) NOT NULL COMMENT '1-Hoạt động, 0-Khóa',
  `last_login` date NOT NULL,
  `token` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_account`
--

INSERT INTO `tbl_account` (`account_id`, `email`, `username`, `address`, `province`, `district`, `ward`, `administrative_address`, `phone`, `coin`, `check_in`, `luckybox`, `role`, `status`, `last_login`, `token`) VALUES
(7, 'maicanhsvc@gmail.com', 'canh mai', '1111', '83', '837', '29185', 'Tỉnh Bến Tre\nHuyện Thạnh Phú\nXã Phú Khánh', '1234567890', 86621, 0, 0, 1, 1, '2024-04-16', 'd52ErAxLRM2jA9kRdNtrm3:APA91bF4sc_HoQ282pQQnFrPpZcL_Kkn7xj9WEyb8O7v-eqUA8-PK-sQW3MfrpVRzPYI1ixW3N8mAUKqmNfQxYqfRORSSeffIbrBzms15oKjWN6q2uNTc3LDG-SyfZ8BMo1ScjrVN98h'),
(11, 'sevycanh1@gmail.com', '', '', '', '', '', '', '', 0, 0, 0, 1, 0, '0000-00-00', ''),
(14, 'sevypee1@gmail.com', '', '', '', '', '', '', '', 0, 1, 3, 1, 1, '2024-04-16', 'd52ErAxLRM2jA9kRdNtrm3:APA91bF4sc_HoQ282pQQnFrPpZcL_Kkn7xj9WEyb8O7v-eqUA8-PK-sQW3MfrpVRzPYI1ixW3N8mAUKqmNfQxYqfRORSSeffIbrBzms15oKjWN6q2uNTc3LDG-SyfZ8BMo1ScjrVN98h'),
(15, 'sevycanh2@gmail.com', '', '', '', '', '', '', '', 0, 1, 3, 1, 1, '2024-04-09', 'ddR7ZwAHTOKgnB1TRaIQA7:APA91bHP9DgLeqvBsz5j3q6vO4hNZPhCdho0WWfeKUHXc-FCewwf0AoILmfffkL9mhCyCymH52nKyQZroxtIYkm_T2GomRKB1mwwP9T-105Uo8tpZfGisBMcJE1Lm6BAY0hbQWhF6IRD'),
(16, 'maicanh2002@gmail.com', '', '', '', '', '', '', '', 50, 0, 3, 0, 1, '2024-04-08', 'dtwvho6-QySuug-wj7o86A:APA91bF9HbVuxRKEcnp5EFTx1ktc5gwmwSsaLz3w4_8hG5qG6eFnqFEfJ1wZhS38DxoVe8EwbGC-J1WhgAWjK6K_Vb605DgqX1OjDxv-SSEGrpDw1IRlOktl-bFTsJKfk5pGtpZ4qZea'),
(17, 'dthinh3005@gmail.com', 'Nguyễn Đình Thịnh', 'Tan Bac', '75', '737', '26278', 'Tỉnh Đồng Nai\nHuyện Trảng Bom\nXã Bình Minh', '0589116773', 29550, 1, 3, 1, 1, '2024-04-15', 'cMVfv4QGTy64caH2uuaJNj:APA91bFa0E86Du3-rA3W_KvKjq3nAqvSx79MVF9xr2sG6Lga3dl2Y5KtqrNRusU5CJHV3ip0YvFQEr8J0uZAum9W8HmbuBStNr3SARg_Y9VvciGfIDbWJpVDxiUkIAe65z0bT6Fh_AdX'),
(18, 'kissuot6@gmail.com', 'Thinh Nguyen', '99 An Dương Vương', '79', '776', '27430', 'Thành phố Hồ Chí Minh\nQuận 8\nPhường 16', '0589116773', 1290, 1, 3, 1, 1, '2024-04-15', 'eoQITwL7RiKdpQEdOnjWWO:APA91bFUYw0FxVxeu8d_wOAd_Y6kaJhT2QVUma4BG77nDZoSMka_IUAl5JKWaD18x_b_IsfTeiXpQP9EBxv37gM26tI__AUljcIBNKbrTbpkvyGLTIhuidtWp1iQzBOz4x9_xepEshH4');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_cart`
--

CREATE TABLE `tbl_cart` (
  `account_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_cart`
--

INSERT INTO `tbl_cart` (`account_id`, `product_id`, `quantity`) VALUES
(7, 73, 1),
(17, 72, 3);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_category`
--

CREATE TABLE `tbl_category` (
  `category_id` int(11) NOT NULL,
  `name` varchar(250) NOT NULL,
  `image` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_category`
--

INSERT INTO `tbl_category` (`category_id`, `name`, `image`) VALUES
(7, 'One Piece', 'a643fa39-f232-4b3e-b629-949d992f5a6b'),
(8, 'Hatsune Miku', '3a2724a7-2ab4-4c40-8dc9-2c4392d27802'),
(9, 'Genshin Impact', 'd2826d9c-a19a-4ff5-ab4f-d7ec84bf3da8'),
(10, 'Dragon Ball', '34ee2da4-c59a-4cd1-8b77-1dd78bd38148'),
(11, 'Naruto', '639e9ee7-a69f-486c-9a30-a428865b8125');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_coupon`
--

CREATE TABLE `tbl_coupon` (
  `coupon_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `code` text NOT NULL,
  `count` int(11) NOT NULL,
  `discount` int(3) NOT NULL,
  `dateFrom` datetime NOT NULL,
  `dateTo` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  `isPublic` int(11) NOT NULL COMMENT '0-false, 1-true'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_coupon`
--

INSERT INTO `tbl_coupon` (`coupon_id`, `name`, `code`, `count`, `discount`, `dateFrom`, `dateTo`, `user_id`, `isPublic`) VALUES
(31, 'Coupon giảm 30% cho đơn hàng', '17-20240409203036', 1, 30, '2024-04-09 20:30:36', '2024-05-09 20:30:36', 17, 0),
(32, 'Khai trương giảm 10% đơn hàng', '0-20240410023028', 997, 10, '2024-04-09 00:00:00', '2024-04-30 23:59:59', 0, 1),
(33, 'Coupon giảm 15% cho đơn hàng', '7-20240416214613', 1, 15, '2024-04-16 21:46:13', '2024-05-16 21:46:13', 7, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_order`
--

CREATE TABLE `tbl_order` (
  `order_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `username` text NOT NULL,
  `address` text NOT NULL,
  `phone` int(11) NOT NULL,
  `payment_method` varchar(100) NOT NULL,
  `coupon_id` int(11) NOT NULL,
  `total` int(11) NOT NULL,
  `order_status` int(10) NOT NULL DEFAULT '1',
  `date` datetime DEFAULT NULL,
  `token` text NOT NULL,
  `app_trans_id` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_order`
--

INSERT INTO `tbl_order` (`order_id`, `account_id`, `username`, `address`, `phone`, `payment_method`, `coupon_id`, `total`, `order_status`, `date`, `token`, `app_trans_id`) VALUES
(122, 17, 'Nguyễn Đình Thịnh', 'Đồng Nai', 589116773, 'COD', 1, 590000, 5, '2024-04-09 19:53:08', '', ''),
(123, 17, 'Nguyễn Đình Thịnh', 'Đồng Nai', 589116773, 'Zalo', 1, 590000, 5, '2024-04-09 20:08:58', 'ACyWX_OQuq1juPYQJBcA6zSA', ''),
(124, 17, 'Nguyễn Đình Thịnh', 'Đồng Nai', 589116773, 'Zalo', 1, 7100000, 1, '2024-04-09 20:24:47', 'ACw9Gs0WwTNXil2qWXGF_Ejw', ''),
(125, 17, 'Nguyễn Đình Thị', 'Đồng Nai', 589116773, 'COD', 1, 63900000, 1, '2024-04-09 20:27:35', '', ''),
(126, 17, 'Nguyễn Đình Thị', 'Đồng Nai', 589116773, 'COD', 1, 590000, 1, '2024-04-09 21:26:48', '', ''),
(127, 17, 'Nguyễn Đình Thị', 'Đồng Nai', 589116773, 'COD', 1, 590000, 1, '2024-04-09 21:27:07', '', ''),
(128, 18, 'Thinh', 'Đồng Nai', 589116773, 'COD', 1, 2950000, 1, '2024-04-09 22:49:13', '', ''),
(129, 18, 'Thinh', 'Đồng Nai', 589116773, 'COD', 1, 590000, 1, '2024-04-09 22:53:37', '', ''),
(130, 7, 'canh mai', '1111 hcm q5', 1234567890, 'COD', 1, 590000, 1, '2024-04-09 23:07:57', '', ''),
(131, 18, 'Thinh', 'Đồng Nai', 589116773, 'COD', 1, 2580000, 1, '2024-04-09 23:09:14', '', ''),
(132, 7, 'canh mai', '1111 hcm q5', 1234567890, 'COD', 1, 590000, 4, '2024-04-09 23:13:01', '', ''),
(133, 18, 'Thinh', 'Đồng Nai', 589116773, 'COD', 1, 2950000, 1, '2024-04-10 00:12:51', '', ''),
(134, 18, 'Thinh', 'Đồng Nai', 589116773, 'COD', 1, 2950000, 1, '2024-04-10 10:41:13', '', ''),
(135, 18, 'Thinh', 'Đồng Nai', 589116773, 'COD', 1, 8220000, 1, '2024-04-10 11:05:15', '', ''),
(136, 18, 'Thinh', 'Đồng Nai', 589116773, 'COD', 1, 2950000, 1, '2024-04-10 11:25:20', '', ''),
(137, 18, 'Thinh', 'Đồng Nai', 589116773, 'COD', 1, 590000, 1, '2024-04-10 11:29:23', '', ''),
(138, 18, 'Thinh', 'Đồng Nai', 589116773, 'COD', 1, 590000, 5, '2024-04-10 11:29:34', '', ''),
(139, 18, 'Thinh', 'Đồng Nai', 589116773, 'COD', 1, 1590000, 1, '2024-04-10 11:30:09', '', ''),
(140, 18, 'Thinh', 'Đồng Nai', 589116773, 'COD', 1, 6950000, 1, '2024-04-10 11:46:48', '', ''),
(141, 18, 'Thinh Nguyen', 'Đồng Nai', 589116773, 'Zalo', 1, 590000, 1, '2024-04-10 13:08:28', 'ACLMXSIsLWw63m6OtKjLfHgA', ''),
(142, 18, 'Thinh Nguyen', 'Đồng Nai', 589116773, 'Zalo', 1, 12900000, 1, '2024-04-10 13:22:58', 'ACyNpt5VNH3vjTw69hA_ddmQ', ''),
(143, 18, 'Thinh Nguyen', 'Đồng Nai', 589116773, 'COD', 1, 2010000, 1, '2024-04-10 16:28:01', '', ''),
(144, 18, 'Thinh Nguyen', 'Đồng Nai', 589116773, 'Zalo', 1, 590000, 1, '2024-04-10 16:28:46', 'AC9L22RN_iAi_jV4VjLTRXUA', ''),
(145, 18, 'Thinh Nguyen', 'Đồng Nai', 589116773, 'Zalo', 1, 590000, 5, '2024-04-11 22:27:36', 'AC9A0UiYXwMMsukdYwM8XHoA', ''),
(146, 18, 'Thinh Nguyen', 'Đồng Nai', 589116773, 'Zalo', 1, 1290000, 4, '2024-04-11 23:20:32', 'ACs7db5RnztGnSR9Njjfvtdw', ''),
(147, 18, 'Thinh Nguyen', 'Đồng Nai', 589116773, 'Zalo', 1, 590000, 2, '2024-04-11 23:48:25', 'AC5Wqv4BHArQT7BkmAFzFDhw', ''),
(148, 18, 'Thinh Nguyen', '99 An Dương Vương, Phường 16, Quận 8, Thành phố Hồ Chí Minh', 589116773, 'Zalo', 1, 590000, 5, '2024-04-14 23:11:16', 'AC2O0RlwmHj_AusR4NLOMqeA', '240414_111100000002'),
(149, 18, 'Thinh Nguyen', '99 An Dương Vương, Phường 16, Quận 8, Thành phố Hồ Chí Minh', 589116773, 'COD', 1, 590000, 5, '2024-04-14 23:12:49', '', ''),
(150, 18, 'Thinh Nguyen', '99 An Dương Vương, Phường 16, Quận 8, Thành phố Hồ Chí Minh', 589116773, 'Zalo', 1, 590000, 5, '2024-04-14 23:13:04', 'ACqBgMnrjVgpYN3Du0yWMw8g', '240414_111254000004'),
(151, 18, 'Thinh Nguyen', '99 An Dương Vương, Xã An Điền, Huyện Thạnh Phú, Tỉnh Bến Tre', 589116773, 'Zalo', 1, 1180000, 5, '2024-04-14 23:20:34', 'ACSMf0GOyYiHvW0y29SI3pQQ', '240414_112021000006'),
(152, 18, 'Thinh Nguyen', '99 An Dương Vương, Phường 16, Quận 8, Thành phố Hồ Chí Minh', 589116773, 'Zalo', 1, 18027000, 5, '2024-04-15 01:07:49', 'ACwmQbgbbk0m5JSyl8DqLaFw', '240415_010731000002'),
(153, 18, 'Thinh Nguyen', '99 An Dương Vương, Phường 16, Quận 8, Thành phố Hồ Chí Minh', 589116773, 'Zalo', 1, 2950000, 5, '2024-04-15 01:09:39', 'ACxQHJG34AM-YcluzZHope9g', '240415_010926000004'),
(154, 18, 'Thinh Nguyen', '99 An Dương Vương, Phường 16, Quận 8, Thành phố Hồ Chí Minh', 589116773, 'Zalo', 1, 5900000, 5, '2024-04-15 11:02:06', 'ACbHe5sTb3DM3jtnU8q5YlDw', '240415_110149000002'),
(155, 18, 'Thinh Nguyen', '99 An Dương Vương, Phường 16, Quận 8, Thành phố Hồ Chí Minh', 589116773, 'Zalo', 1, 5310000, 5, '2024-04-15 22:04:47', 'ACfo6pVBfdRCqLDggt4yeEVg', '240415_100436000004'),
(156, 7, 'canh mai', '1111, Xã Phú Khánh, Huyện Thạnh Phú, Tỉnh Bến Tre', 1234567890, 'COD', 1, 531000, 4, '2024-04-16 21:52:06', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_orderdetail`
--

CREATE TABLE `tbl_orderdetail` (
  `detail_id` int(100) NOT NULL,
  `order_id` int(100) NOT NULL,
  `product_id` int(100) NOT NULL,
  `quantity` int(100) NOT NULL,
  `unitPrice` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_orderdetail`
--

INSERT INTO `tbl_orderdetail` (`detail_id`, `order_id`, `product_id`, `quantity`, `unitPrice`) VALUES
(81, 122, 71, 1, 590000),
(82, 123, 72, 1, 590000),
(83, 124, 73, 5, 1420000),
(84, 125, 73, 45, 1420000),
(85, 126, 72, 1, 590000),
(86, 127, 72, 1, 590000),
(87, 128, 72, 5, 590000),
(88, 129, 34, 1, 590000),
(89, 130, 71, 1, 590000),
(90, 131, 33, 2, 1290000),
(91, 132, 71, 1, 590000),
(92, 133, 72, 5, 590000),
(93, 134, 72, 5, 590000),
(94, 135, 70, 5, 1290000),
(95, 135, 71, 3, 590000),
(96, 136, 71, 5, 590000),
(97, 137, 72, 1, 590000),
(98, 138, 71, 1, 590000),
(99, 139, 69, 1, 1590000),
(100, 140, 69, 4, 1590000),
(101, 140, 71, 1, 590000),
(102, 141, 72, 1, 590000),
(103, 142, 70, 10, 1290000),
(104, 143, 66, 1, 1420000),
(105, 143, 65, 1, 590000),
(106, 144, 64, 1, 590000),
(107, 145, 72, 1, 590000),
(108, 146, 70, 1, 1290000),
(109, 147, 71, 1, 590000),
(110, 148, 72, 1, 590000),
(111, 149, 71, 1, 590000),
(112, 150, 71, 1, 590000),
(113, 151, 71, 2, 590000),
(114, 152, 32, 10, 1590000),
(115, 152, 71, 4, 590000),
(116, 152, 72, 3, 590000),
(117, 153, 71, 5, 590000),
(118, 154, 71, 10, 590000),
(119, 155, 72, 9, 590000),
(120, 156, 71, 1, 590000);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_product`
--

CREATE TABLE `tbl_product` (
  `product_id` int(11) NOT NULL,
  `name` varchar(250) NOT NULL,
  `price` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `description` text NOT NULL,
  `main_image` text NOT NULL,
  `sub_image` text NOT NULL,
  `coupon` int(11) NOT NULL DEFAULT '0',
  `category_id` int(11) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '1-visible 0-invisible'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tbl_product`
--

INSERT INTO `tbl_product` (`product_id`, `name`, `price`, `quantity`, `description`, `main_image`, `sub_image`, `coupon`, `category_id`, `status`) VALUES
(32, 'MONKEY D. LUFFY GEAR 5 ICHIBAN KUJI ONE PIECE SHIN YONKOU (BANDAI)', 1590000, 10, 'Sản phẩm chính hãng nội địa Nhật Bản Monkey D. Luffy Gear 5 Ichiban Kuji One Piece Shin Yonkou (Bandai)\nHãng sản xuất: Bandai Spirits\nKích thước: 20 cm\nChất liệu: PVC', '1_7af3383a-f85a-4c44-ab65-534fd72fc310', '1_9806f2ce-7959-47be-ac1d-07fdeef1f978', 0, 7, 1),
(33, 'EUSTASS KID ICHIBAN KUJI ONE PIECE BEYOND THE LEVEL', 1290000, 10, 'Sản phẩm chính hãng nội địa Nhật Bản Eustass Kid Ichiban Kuji One Piece Beyond the Level\nHãng sản xuất: Bandai\nKích thước: 20 cm\nChất liệu: PVC - ABS', '33_44ed7748-59c0-4d20-bb84-f6f62927ba5d', '33_e77a8630-cb4e-4356-a55f-629b4d525f5c,33_e55520d4-27a3-43d9-bb0c-3538b6b13839,33_e98fd4a4-0ef6-4573-9cd8-7e40c59f74ec', 0, 7, 1),
(34, 'RORONOA ZORO DXF FIGURE THE GRANDLINE SERIES WANO COUNTRY', 590000, 51, 'Sản phẩm chính hãng nội địa Nhật Bản Roronoa Zoro DXF Figure The Grandline Series Wano Country\nHãng sản xuất: Bandai\nKích thước: 17 cm\nChất liệu: PVC', '34_96e99b2d-e521-4350-9a38-30abbc76aaf7', '34_3ae7f45c-a1dd-45ad-9408-eba5ac932168,34_55194cf3-661e-4bf8-831f-659d1d1f0213', 0, 7, 1),
(35, 'RORONOA ZORO DXF FIGURE THE GRANDLINE SERIES', 590000, 51, 'Roronoa Zoro DXF Figure The Grandline Series \nHãng sản xuất: Bandai\nKích thước: 17 cm\nChất liệu: PVC - ABS', '35_e40c66e4-92d4-4f70-aadc-5827a21d98b8', '35_13499735-34e7-44e7-aba3-b6c6d88cb370,35_8f3d52ed-eee6-4f77-af8d-8901eed3765a', 5, 7, 1),
(36, 'KING IMPREGNABLE SWORD ICHIBAN KUJI', 1420000, 50, 'ICHIBAN KUJI ONE PIECE IMPREGNABLE SWORD – FIGURINE KING \nDòng : Ichiban Kuji\nHãng sản xuất : Bandai\nKích thước 14cm \nChất liệu : PVC - ABS', '36_b10f95bc-6c37-4820-8185-08fe6adc2b4a', '36_e4468b2c-1885-4972-acf7-c5ff7d1c113a,36_86db121f-3b34-4f1e-b94d-a78024d392b6', 10, 7, 1),
(37, 'TAITO KUJI HATSUNE MIKU 39 (MIKU) NO HI KINEN KUJI 2ND SEASON', 1490000, 50, 'Taito Kuji Hatsune Miku 39 (Miku) no hi kinen kuji 2nd season \nHãng sản xuất: Taito \nKích thước: 20cm', '37_6a797e1c-9146-4abf-a7ad-288f3e6cd73a', '37_e23b319e-34ad-4fea-bc5f-8c48c41124e0,37_178becd0-8fe4-4883-bd19-2a29e0456c0c,37_82cb317e-8da7-48ab-9ae6-a72314cdeb67', 10, 8, 1),
(38, 'TAITO KUJI HATSUNE MIKU 39 (MIKU) NO HI KINEN KUJI 2ND SEASON', 1790000, 50, 'Taito Kuji Hatsune Miku 39 (Miku) no hi kinen kuji 2nd season\nHãng sản xuất: Taito\nKích thước: 20cm', '38_469f1d1c-4927-4192-b5ec-b8f4783f8865', '38_12283931-a3b6-42de-80f7-263d8fb9e970,38_38e8f463-da75-4d17-8e33-dd00a9d4aebd,38_a06864a2-dd41-4aad-a9c7-9ec53437239a', 0, 8, 1),
(39, 'HATSUNE MIKU - RODY - ARTIST MASTERPIECE', 570000, 50, 'Hatsune Miku - Rody - Artist MasterPiece+ - 39 ver \nHãng sản xuất: Taito \nKích thước: 23cm \nChất liệu: ABS - PVC', '39_b631d209-18a3-4db3-8fab-e14300a3745a', '39_657a5bb4-8758-4624-8891-cd09ddd5e225,39_429fc79c-383e-4824-b302-7d676bfd53b8,39_b73933da-8cbf-487e-a080-a61806b8b410,39_a45ade88-23f3-4deb-a447-fbacb6315b7c', 0, 8, 1),
(40, 'PIAPRO CHARACTERS - HATSUNE MIKU - LUMINASTA - MODERN CHINA (SEGA)', 540000, 50, 'Hãng sản xuất: SEGA\n Kích thước:  18cm\n Chất liệu: PVC - ABS', '40_2860b1ac-17fd-4706-a72b-bf88e00bd40b', '40_9bce2593-67a9-48cd-a686-42d9cee3327c,40_aa9b5f88-2ed0-441b-91d9-99869cf9f419,40_e2d14b8f-748c-4207-b7d7-98f2de618f4d', 0, 8, 1),
(41, 'HATSUNE MIKU FASHION FIGURE UNIFORM (TAITO)', 490000, 50, 'Hatsune Miku Fashion Figure Uniform (Taito)\nHãng sản xuất: Taito\nKích thước: 18 cm\nChất liệu: PVC', '41_0cfa42c8-520b-40ea-824a-259d5f92fa2e', '41_389f08a9-0b23-47d0-906b-9b5f06aa9373,41_de7725eb-a896-4aca-b19e-2bf34928db47,41_3f742f7e-ac4a-4037-ba9b-db7fcbb19d43,41_bcd8eb20-598f-4a6d-b722-cd6ca1e69df4', 0, 8, 1),
(42, 'GANYU - GENSHIN IMPACT (CHÍNH HÃNG)', 349000, 50, 'Mô Hình Ganyu - Genshin Impact\n\nSeries: Genshin Impact\n\nHãng sản xuất: APEX Toys x mihoyo\n\nTỉ lệ: 1/7\n\nChiều cao: 22cm', '42_e9efa463-c2e4-487b-93b7-61fac8e5d134', '42_0f85470a-ea51-43ad-bbcc-bdf344e10632,42_956d29f3-7fce-4281-b5b3-11488e78ae4e,42_d20931ea-7352-4dca-b825-1014e5541dd4,42_5945422e-c44f-4cb8-ad26-18b9b8b02d66', 0, 9, 1),
(43, 'GENSHIN IMPACT - HU TAO - HAPPY SHAKE (APEX INNOVATION)', 520000, 50, 'Genshin Impact - Hu Tao - Happy Shake (Apex Innovation)\n\nKích thước : 10cm x 6cm x 4,5cm', '43_4693cbe0-8a50-4849-b927-6e0edfb16fa2', '43_3295e9e7-77e3-456a-a576-f92d89b6437d,43_f8c87b02-50c9-4dec-b1f6-06701bf750bb,43_188077f8-6153-40ab-b64e-17997310beab,43_862878a7-2548-47bb-869e-ca755872816c,43_9e229fff-858f-4f56-b31d-6ffd37b8cfb3', 3, 9, 1),
(44, 'GENSHIN IMPACT XIAO: VIGILANT YAKSHA VER. 1/7 SCALE FIGURE (CHÍNH HÃNG MIHOYO)', 4650000, 50, 'Genshin Impact Xiao: Vigilant Yaksha ver. 1/7 Scale Figure\n\nKích thước : chiều dài 28cm x rộng 28cm x cao 27cm\n\nPhát Hành : tháng 6 năm 2023', '44_a7e18d46-ebd5-4dc5-85f5-fa5930f02a57', '44_0eaf660d-d803-4626-9a0f-ac4f3be8e08a,44_f2fd4616-1627-4478-a5ba-7434b8418ff3,44_e6d92d92-6a49-4d92-a174-897bae5d1266,44_54620ea9-85c1-48f5-b680-c0d47fbb087a', 0, 9, 1),
(45, 'FIGMA MONA \"MIRROR REFLECTION OF DOOM\" VER - GENSHIN IMPACT (CHÍNH HÃNG)', 2790000, 50, 'Series: Genshin Impact\nHãng sản xuất:Max Factory\n\nChất liệu: ABS&PVC\n\nChiều cao: 150mm', '45_432295b9-bf86-4d42-a57d-698db41420ae', '45_81a56d4e-a975-4c37-b771-8cc412e60edc,45_9de7e4b2-31ba-4b86-ae83-3b2815f50669,45_958caaa3-25f8-4d71-a49b-b4aca3893246,45_a7991a64-ea9b-4379-a475-879a482db1a7,45_de55c303-4d78-4e6d-8b2f-3ba816e2132d,45_c4ff4966-1d27-46dc-9270-f8db62c78827,45_803ad949-f83e-4052-b34c-9db933ef4953,45_8bd1c3c6-4ad8-4518-aaee-11c83c53bdc3', 0, 9, 1),
(46, 'LUMINE 1/7 COMPLETE FIGURE - GENSHIN IMPACT (CHÍNH HÃNG)', 4290000, 50, 'Series: Genshin Impact\n\nHãng sản xuất: KOTOBUKIYA\n\nTỉ lệ mô hình: 1/7\n\nChiều cao:  25cm\n\nChất liệu: PVC ABS', '46_da7723a0-a84a-4ba7-aeb3-949504336aa5', '46_c9d6ea2c-50d0-486d-b9e4-ea80a7af4c9c,46_4f012f16-f02c-4e66-9a6c-30474afb3b82', 0, 9, 1),
(47, 'HATAKE KAKASHI - NARUTO NARUTOP99 (BANDAI SPIRITS)\n', 530000, 50, 'Hatake Kakashi - Naruto NARUTOP99 (Bandai Spirits)\n\nHãng sản xuất: Bandai Spirits\n\nKích thước: 14 cm\n\nChất liệu: PVC', '47_c3789863-fb7e-4a14-af5c-88a2dc3005c4', '47_6874f2af-9323-41ce-9046-121dab2bb45b', 0, 11, 1),
(48, 'NARUTO 20TH ANNIVERSARY - BANPRESTO BANDAI', 550000, 50, 'Sản phẩm chính hãng nội địa Nhật Bản\n \nHãng sản xuất: Bandai\n\nChất liệu: PVC - ABS', '48_94df8c3c-949b-488e-9bc3-d3eca84dc9ff', '48_dc4921a0-ae92-4d13-b9f1-c861c5b27a16,48_4aabb1ab-0586-4a90-9746-86968586cc40', 0, 11, 1),
(49, 'NARUTO SHIPPUDEN VIBRATION STARS SASUKE UCHIHA VS DEIDARA ( A UCHIHA SASUKE )', 550000, 50, 'Hãng sản xuất: Bandai \n \n Kích thước : 16cm \n \n Chất liệu : PVC - ABS', '49_fd7dff43-c4ee-4ef2-ba38-24941f4b0208', '49_a4c6ec4f-2536-4578-b440-c60161927d19,49_9fff2f3d-620b-4384-99cb-e0899315f082', 0, 11, 1),
(50, 'UZUMAKI NARUTO VIBRATION STARS IV - NARUTO SHIPPUUDEN (BANDAI SPIRITS)', 490000, 50, 'Uzumaki Naruto Vibration Stars IV - Naruto Shippuuden (Bandai Spirits)\n\nHãng sản xuất: Bandai\n\nKích thước: 16 cm\n\nChất liệu: PVC', '50_1008bc02-8fc4-49db-8302-b0c4c0b46e83', '50_2a70b603-0397-49dc-a38c-ea89bbb5ce00,50_f0361d73-bbb1-472b-bdc6-eaa996ab446d,50_b4829591-3e56-419f-ab9f-2651789c5233', 0, 11, 1),
(51, 'NARUTO UZUMAKI -BORUTO- KIZUNA RELATION - BORUTO NARUTO NEXT GENERATIONS (CHÍNH HÃNG)', 2550000, 50, 'Series: BORUTO NARUTO NEXT GENERATIONS\n\n❄️Hãng sản xuất : Bandai\n\n❄️Chất liệu: PVC ABS\n\n❄️Kích thước : 20 cm\n\n❄️Phát hành: T9/2023', '51_7948fa89-327b-4619-af8c-f47e51f89458', '51_cb2f02d7-a5e5-4cbd-81e8-945ba4b74542', 0, 11, 1),
(52, 'Shan He Studio - Majin Vegeta 1/4', 4100000, 50, 'Tỉ lệ 1/4 - Chiều cao 41CM \nTỉ lệ 1/6 - Chiều cao 28CM \nTỉ lệ 1/7 - Chiều cao 24CM \nNote : Chọn 1 trong 2 đầu A và B', '52_68b856a3-ffc3-4a12-8491-8a80d0fc73e2', '52_f8396960-5cbb-4a1b-9a4d-e1bb30b4ad38,52_f49f2266-c186-480d-bb49-453d8708d4a8', 0, 10, 1),
(53, 'Frieza Cell Buu - Hero Belief Studio', 21000000, 50, 'Kích thước :  55cm (cao) x 38.7cm (rộng) x 34.3cm (sâu)\n\nHãng sản xuất:   Hero Belief Studio\n\nPhát hành: Q1-2/2023', '53_6cfbc40b-5187-4d68-b7e6-736c05ad99c3', '53_01a95168-4fd4-490e-a024-78670a90854c,53_3b23072c-a037-49bc-8056-e982047d71de,53_f0d0a159-b86a-4d03-9b3d-d36b95fa48d5,53_e0c9251e-3340-4229-ade6-ea6e700324a7', 0, 10, 1),
(54, 'King Broly - Z Studio ', 5550000, 50, 'Hãng sản xuất: Z Studio\n\nPhát hành: Q4/2023  - 188 bản', '54_67f64f6b-cc6e-4d80-a0cd-7b5e4bcd6eed', '54_71c47913-4589-45cb-864d-1993e270b941,54_fd642480-08f2-459f-a6b5-9dadf3253931,54_a1f4a443-e8de-4a01-ab87-096e7901df22', 0, 10, 1),
(55, 'Frieza Form 4 - Prime 1 studio x Mega House', 40900000, 50, 'Tỉ lệ 1/4: [H]61cm x [W]43cm x [D]34cm\nHãng sản xuất: Prime 1 studio x Mega House\n\nPhát hành: Q4/2024 - Q2/2025 - 499 bản', '55_db61b59e-8275-4cc6-8c7b-e45cab3e2947', '55_d4ea2c15-a50c-41a2-99f7-d85f0a87db9d,55_f6f2104a-9fd7-4256-a70d-cdac0917f101,55_2dd24c31-2c59-4536-96dc-a7d3373a5644,55_2441362b-b69c-4e60-9f2a-36eefe5bbdf7', 0, 10, 1),
(56, 'Vegeta SSJ4 Bust 1/1 - MAD x MRC studio', 23900000, 50, 'Kích thước: [H]95cm x [W]86cm x [D]67cm\nHãng sản xuất: MAD x MRC studio\n\nNote: Bản VIP có led 4 chiều\n\nPhát hành: Q1/2024 - 80 bản', '56_d7803001-9327-4a0f-a46b-6eb9ba345938', '56_408031c9-6c29-4f02-9932-355547fe238f,56_2e689839-3264-4284-b56b-a3dc0c2c3b3c,56_9c3faafc-6904-47af-8c12-02606a950c33', 0, 10, 1),
(57, 'MONKEY D. LUFFY GEAR 5 ICHIBAN KUJI ONE PIECE SHIN YONKOU (BANDAI)', 1590000, 50, 'Sản phẩm chính hãng nội địa Nhật Bản Monkey D. Luffy Gear 5 Ichiban Kuji One Piece Shin Yonkou (Bandai)\r\nHãng sản xuất: Bandai Spirits\r\nKích thước: 20 cm\r\nChất liệu: PVC', '1_7af3383a-f85a-4c44-ab65-534fd72fc310', '1_9806f2ce-7959-47be-ac1d-07fdeef1f978', 0, 7, 1),
(58, 'EUSTASS KID ICHIBAN KUJI ONE PIECE BEYOND THE LEVEL', 1290000, 50, 'Sản phẩm chính hãng nội địa Nhật Bản \'Eustass Kid Ichiban Kuji One Piece Beyond the Level\r\nHãng sản xuất: Bandai\r\nKích thước: 20 cm\r\nChất liệu: PVC - ABS', '33_44ed7748-59c0-4d20-bb84-f6f62927ba5d', '33_e77a8630-cb4e-4356-a55f-629b4d525f5c,33_e55520d4-27a3-43d9-bb0c-3538b6b13839,33_e98fd4a4-0ef6-4573-9cd8-7e40c59f74ec', 0, 7, 1),
(59, 'RORONOA ZORO DXF FIGURE THE GRANDLINE SERIES WANO COUNTRY', 590000, 50, 'Sản phẩm chính hãng nội địa Nhật Bản Roronoa Zoro DXF Figure The Grandline Series Wano Country\r\nHãng sản xuất: Bandai\r\nKích thước: 17 cm\r\nChất liệu: PVC', '34_96e99b2d-e521-4350-9a38-30abbc76aaf7', '34_3ae7f45c-a1dd-45ad-9408-eba5ac932168,34_55194cf3-661e-4bf8-831f-659d1d1f0213', 0, 7, 1),
(60, 'RORONOA ZORO DXF FIGURE THE GRANDLINE SERIES', 590000, 50, 'Roronoa Zoro DXF Figure The Grandline Series \r\nHãng sản xuất: Bandai\r\nKích thước: 17 cm\r\nChất liệu: PVC - ABS', '35_e40c66e4-92d4-4f70-aadc-5827a21d98b8', '35_13499735-34e7-44e7-aba3-b6c6d88cb370,35_8f3d52ed-eee6-4f77-af8d-8901eed3765a', 0, 7, 1),
(61, 'KING IMPREGNABLE SWORD ICHIBAN KUJI', 1420000, 50, 'ICHIBAN KUJI ONE PIECE IMPREGNABLE SWORD – FIGURINE KING \r\nDòng : Ichiban Kuji\r\nHãng sản xuất : Bandai\r\nKích thước 14cm \r\nChất liệu : PVC - ABS', '36_b10f95bc-6c37-4820-8185-08fe6adc2b4a', '36_e4468b2c-1885-4972-acf7-c5ff7d1c113a,36_86db121f-3b34-4f1e-b94d-a78024d392b6', 0, 7, 1),
(62, 'MONKEY D. LUFFY GEAR 5 ICHIBAN KUJI ONE PIECE SHIN YONKOU (BANDAI)', 1590000, 50, 'Sản phẩm chính hãng nội địa Nhật Bản Monkey D. Luffy Gear 5 Ichiban Kuji One Piece Shin Yonkou (Bandai)\r\nHãng sản xuất: Bandai Spirits\r\nKích thước: 20 cm\r\nChất liệu: PVC', '1_7af3383a-f85a-4c44-ab65-534fd72fc310', '1_9806f2ce-7959-47be-ac1d-07fdeef1f978', 0, 7, 1),
(63, 'EUSTASS KID ICHIBAN KUJI ONE PIECE BEYOND THE LEVEL', 1290000, 50, 'Sản phẩm chính hãng nội địa Nhật Bản \'Eustass Kid Ichiban Kuji One Piece Beyond the Level\r\nHãng sản xuất: Bandai\r\nKích thước: 20 cm\r\nChất liệu: PVC - ABS', '33_44ed7748-59c0-4d20-bb84-f6f62927ba5d', '33_e77a8630-cb4e-4356-a55f-629b4d525f5c,33_e55520d4-27a3-43d9-bb0c-3538b6b13839,33_e98fd4a4-0ef6-4573-9cd8-7e40c59f74ec', 0, 7, 1),
(64, 'RORONOA ZORO DXF FIGURE THE GRANDLINE SERIES WANO COUNTRY', 590000, 49, 'Sản phẩm chính hãng nội địa Nhật Bản Roronoa Zoro DXF Figure The Grandline Series Wano Country\r\nHãng sản xuất: Bandai\r\nKích thước: 17 cm\r\nChất liệu: PVC', '34_96e99b2d-e521-4350-9a38-30abbc76aaf7', '34_3ae7f45c-a1dd-45ad-9408-eba5ac932168,34_55194cf3-661e-4bf8-831f-659d1d1f0213', 0, 7, 1),
(65, 'RORONOA ZORO DXF FIGURE THE GRANDLINE SERIES', 590000, 49, 'Roronoa Zoro DXF Figure The Grandline Series \r\nHãng sản xuất: Bandai\r\nKích thước: 17 cm\r\nChất liệu: PVC - ABS', '35_e40c66e4-92d4-4f70-aadc-5827a21d98b8', '35_13499735-34e7-44e7-aba3-b6c6d88cb370,35_8f3d52ed-eee6-4f77-af8d-8901eed3765a', 0, 7, 1),
(66, 'KING IMPREGNABLE SWORD ICHIBAN KUJI', 1420000, 49, 'ICHIBAN KUJI ONE PIECE IMPREGNABLE SWORD – FIGURINE KING \r\nDòng : Ichiban Kuji\r\nHãng sản xuất : Bandai\r\nKích thước 14cm \r\nChất liệu : PVC - ABS', '36_b10f95bc-6c37-4820-8185-08fe6adc2b4a', '36_e4468b2c-1885-4972-acf7-c5ff7d1c113a,36_86db121f-3b34-4f1e-b94d-a78024d392b6', 0, 7, 1),
(67, 'GANYU - GENSHIN IMPACT (CHÍNH HÃNG)', 3490000, 50, 'Mô Hình Ganyu - Genshin Impact\r\n\r\nSeries: Genshin Impact\r\n\r\nHãng sản xuất: APEX Toys x mihoyo\r\n\r\nTỉ lệ: 1/7\r\n\r\nChiều cao: 22cm', '42_e9efa463-c2e4-487b-93b7-61fac8e5d134', '42_0f85470a-ea51-43ad-bbcc-bdf344e10632,42_956d29f3-7fce-4281-b5b3-11488e78ae4e,42_d20931ea-7352-4dca-b825-1014e5541dd4,42_5945422e-c44f-4cb8-ad26-18b9b8b02d66', 0, 9, 1),
(68, 'GENSHIN IMPACT XIAO: VIGILANT YAKSHA VER. 1/7 SCALE FIGURE (CHÍNH HÃNG MIHOYO)', 4650000, 50, 'Genshin Impact Xiao: Vigilant Yaksha ver. 1/7 Scale Figure\r\n\r\nKích thước : chiều dài 28cm x rộng 28cm x cao 27cm\r\n\r\nPhát Hành : tháng 6 năm 2023', '44_a7e18d46-ebd5-4dc5-85f5-fa5930f02a57', '44_0eaf660d-d803-4626-9a0f-ac4f3be8e08a,44_f2fd4616-1627-4478-a5ba-7434b8418ff3,44_e6d92d92-6a49-4d92-a174-897bae5d1266,44_54620ea9-85c1-48f5-b680-c0d47fbb087a', 0, 9, 1),
(69, 'MONKEY D. LUFFY GEAR 5 ICHIBAN KUJI ONE PIECE SHIN YONKOU (BANDAI)', 1590000, 45, 'Sản phẩm chính hãng nội địa Nhật Bản Monkey D. Luffy Gear 5 Ichiban Kuji One Piece Shin Yonkou (Bandai)\r\nHãng sản xuất: Bandai Spirits\r\nKích thước: 20 cm\r\nChất liệu: PVC', '1_7af3383a-f85a-4c44-ab65-534fd72fc310', '1_9806f2ce-7959-47be-ac1d-07fdeef1f978', 0, 7, 1),
(70, 'EUSTASS KID ICHIBAN KUJI ONE PIECE BEYOND THE LEVEL', 1290000, 34, 'Sản phẩm chính hãng nội địa Nhật Bản \'Eustass Kid Ichiban Kuji One Piece Beyond the Level\r\nHãng sản xuất: Bandai\r\nKích thước: 20 cm\r\nChất liệu: PVC - ABS', '33_44ed7748-59c0-4d20-bb84-f6f62927ba5d', '33_e77a8630-cb4e-4356-a55f-629b4d525f5c,33_e55520d4-27a3-43d9-bb0c-3538b6b13839,33_e98fd4a4-0ef6-4573-9cd8-7e40c59f74ec', 0, 7, 1),
(71, 'RORONOA ZORO DXF FIGURE THE GRANDLINE SERIES WANO COUNTRY', 590000, 36, 'Sản phẩm chính hãng nội địa Nhật Bản Roronoa Zoro DXF Figure The Grandline Series Wano Country\r\nHãng sản xuất: Bandai\r\nKích thước: 17 cm\r\nChất liệu: PVC', '34_96e99b2d-e521-4350-9a38-30abbc76aaf7', '34_3ae7f45c-a1dd-45ad-9408-eba5ac932168,34_55194cf3-661e-4bf8-831f-659d1d1f0213', 0, 7, 1),
(72, 'RORONOA ZORO DXF FIGURE THE GRANDLINE SERIES', 590000, 12, 'Roronoa Zoro DXF Figure The Grandline Series \r\nHãng sản xuất: Bandai\r\nKích thước: 17 cm\r\nChất liệu: PVC - ABS', '35_e40c66e4-92d4-4f70-aadc-5827a21d98b8', '35_13499735-34e7-44e7-aba3-b6c6d88cb370,35_8f3d52ed-eee6-4f77-af8d-8901eed3765a', 0, 7, 0),
(73, 'KING IMPREGNABLE SWORD ICHIBAN KUJI', 1420000, 0, 'ICHIBAN KUJI ONE PIECE IMPREGNABLE SWORD – FIGURINE KING \r\nDòng : Ichiban Kuji\r\nHãng sản xuất : Bandai\r\nKích thước 14cm \r\nChất liệu : PVC - ABS', '36_b10f95bc-6c37-4820-8185-08fe6adc2b4a', '36_e4468b2c-1885-4972-acf7-c5ff7d1c113a,36_86db121f-3b34-4f1e-b94d-a78024d392b6', 5, 7, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_account`
--
ALTER TABLE `tbl_account`
  ADD PRIMARY KEY (`account_id`);

--
-- Indexes for table `tbl_cart`
--
ALTER TABLE `tbl_cart`
  ADD PRIMARY KEY (`account_id`,`product_id`);

--
-- Indexes for table `tbl_category`
--
ALTER TABLE `tbl_category`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `tbl_coupon`
--
ALTER TABLE `tbl_coupon`
  ADD PRIMARY KEY (`coupon_id`);

--
-- Indexes for table `tbl_order`
--
ALTER TABLE `tbl_order`
  ADD PRIMARY KEY (`order_id`);

--
-- Indexes for table `tbl_orderdetail`
--
ALTER TABLE `tbl_orderdetail`
  ADD PRIMARY KEY (`detail_id`);

--
-- Indexes for table `tbl_product`
--
ALTER TABLE `tbl_product`
  ADD PRIMARY KEY (`product_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_account`
--
ALTER TABLE `tbl_account`
  MODIFY `account_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `tbl_category`
--
ALTER TABLE `tbl_category`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `tbl_coupon`
--
ALTER TABLE `tbl_coupon`
  MODIFY `coupon_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `tbl_order`
--
ALTER TABLE `tbl_order`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=157;

--
-- AUTO_INCREMENT for table `tbl_orderdetail`
--
ALTER TABLE `tbl_orderdetail`
  MODIFY `detail_id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=121;

--
-- AUTO_INCREMENT for table `tbl_product`
--
ALTER TABLE `tbl_product`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=74;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
