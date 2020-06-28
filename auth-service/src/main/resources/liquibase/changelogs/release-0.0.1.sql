--liquibase formatted mqsql
--changeset cq:1

CREATE TABLE IF NOT EXISTS `auth`.`oauth_client_details` (
       client_id VARCHAR(256) PRIMARY KEY,
       resource_ids VARCHAR(256),
       client_secret VARCHAR(256),
       scope VARCHAR(256),
       authorized_grant_types VARCHAR(256),
       web_server_redirect_uri VARCHAR(256),
       authorities VARCHAR(256),
       access_token_validity INTEGER,
       refresh_token_validity INTEGER,
       additional_information VARCHAR(4096),
       autoapprove VARCHAR(256)
);

INSERT INTO oauth_client_details    (client_id, client_secret, scope, authorized_grant_types,    web_server_redirect_uri, authorities, access_token_validity,    refresh_token_validity, additional_information, autoapprove)
VALUES    ('user-client', '$2a$10$o2l5kA7z.Caekp72h5kU7uqdTDrlamLq.57M1F6ulJln9tRtOJufq', 'all',    'authorization_code,refresh_token,password', null, null, 3600, 36000, null, true);

CREATE TABLE IF NOT EXISTS `auth`.`user` (
    id BIGINT UNSIGNED NOT NULL COMMENT "uid",
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间戳",
    nickname VARCHAR(32) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL COMMENT "昵称",
    avatar VARCHAR(256) NULL COMMENT "头像",
    phone VARCHAR(13) CHARACTER SET 'utf8' NOT NULL COMMENT "手机号",
    area VARCHAR(4) CHARACTER SET 'utf8' NOT NULL COMMENT "国际区号",
    password VARCHAR(256) CHARACTER SET 'utf8' NOT NULL COMMENT "密码",
    salt VARCHAR(12) CHARACTER SET 'utf8' NULL COMMENT "盐",
    PRIMARY KEY (id),
    UNIQUE key (phone)
)
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `auth`.`role` (
    id INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT "uid",
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间戳",
    name VARCHAR(32) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL COMMENT "角色名称",
    PRIMARY KEY (id)
)ENGINE = InnoDB;

INSERT INTO `role` (`id`,`name`)VALUE ("1","app");

CREATE TABLE IF NOT EXISTS `auth`.`user_role`(
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT "创建时间戳",
    uid BIGINT UNSIGNED NOT NULL COMMENT "uid",
    rid BIGINT UNSIGNED NOT NULL COMMENT "role_id",
    UNIQUE key (uid)
)ENGINE = InnoDB;

INSERT INTO `user` (`id`, `create_at`, `nickname`, `phone`, `area`, `password`, `salt`)
VALUES ('305703314103734271', '2020-06-17 15:16:11', 'jdzPD', '13320219007', '86', '$2a$10$GjwmPRczd.9CasC0kYTLWO9PyzgqCfc8vFH7MPrLQcj09ba4JUfi6', NULL);

INSERT INTO `user_role` (`uid`,`rid`)VALUE ("305703314103734271","1");



