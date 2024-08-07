-- member 테이블 생성
CREATE TABLE `member` (
    `member_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `address` VARCHAR(255),
    `del` BOOLEAN DEFAULT FALSE,
    `social` BOOLEAN DEFAULT FALSE,
    `cart_id` BIGINT
);

-- role 테이블 생성
CREATE TABLE `role` (
    `role_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `role_name` VARCHAR(50) NOT NULL UNIQUE
);

-- member_roles 테이블 생성
CREATE TABLE `member_roles` (
    `member_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`member_id`, `role_id`),
    FOREIGN KEY (`member_id`) REFERENCES `member`(`member_id`) ON DELETE CASCADE,
    FOREIGN KEY (`role_id`) REFERENCES `role`(`role_id`) ON DELETE CASCADE
);


-- 초기 역할 데이터 삽입
INSERT INTO `role` (`role_name`) VALUES ('USER'), ('MANAGER'), ('ADMIN');

-- 샘플 회원 삽입
INSERT INTO `member` (`name`, `email`, `password`, `address`)
VALUES ('John Doe', 'user1@zerock.com', '1234', '1234 Elm St');

-- 회원 역할 관계 삽입
INSERT INTO `member_roles` (`member_id`, `role_id`)
VALUES (1, 1), (1, 2);  -- John Doe에게 USER 및 ADMIN 역할 부여
