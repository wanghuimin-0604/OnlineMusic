-- 数据库
drop database if exists musicserver;
create database if not exists musicserver character set utf8;
-- 使用数据库
use musicserver;
DROP TABLE IF EXISTS music;
--创建歌曲表
CREATE TABLE music (
id int PRIMARY KEY AUTO_INCREMENT,
title varchar(50) NOT NULL,
singer varchar(30) NOT NULL,
time varchar(13) NOT NULL,
url varchar(100) NOT NULL,
userid int(11) NOT NULL
);
DROP TABLE IF EXISTS user;
--创建用户信息表
CREATE TABLE user (
id INT PRIMARY KEY AUTO_INCREMENT,
username varchar(20) NOT NULL,
password varchar(32) NOT NULL,
age INT NOT NULL,
gender varchar(2) NOT NULL,
email varchar(50) NOT NULL
);
DROP TABLE IF EXISTS lovemusic;
--创建喜欢音乐表
CREATE TABLE lovemusic (
id int PRIMARY KEY AUTO_INCREMENT,
user_id int(11) NOT NULL,
music_id int(11) NOT NULL
);
INSERT INTO user(username,password,age,gender,email)
VALUES("wanghuimin","123","10","女","1262913815@qq.com");