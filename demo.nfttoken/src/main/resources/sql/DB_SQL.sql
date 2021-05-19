CREATE TABLE `nft_transfer` (
   `id` int NOT NULL AUTO_INCREMENT,
   `contract_hash` varchar(45) NOT NULL,
   `tokenId` varchar(64) NOT NULL,
   `from_address` varchar(45) NOT NULL,
   `to_address` varchar(45) NOT NULL,
   `amount` int NOT NULL,
   `tx_hash` varchar(64) NOT NULL,
   `block_time` bigint DEFAULT NULL,
   PRIMARY KEY (`id`),
   KEY `from_index` (`from_address`),
   KEY `to_index` (`to_address`),
   KEY `tokenId_index` (`tokenId`)
 ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

 CREATE TABLE `block_status` (
    `id` int NOT NULL AUTO_INCREMENT,
    `block_index` int NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `blockIndex_UNIQUE` (`block_index`)
  ) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci