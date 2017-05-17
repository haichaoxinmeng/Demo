CREATE DATABASE WT_DEV;
USE WT_DEV;
CREATE TABLE temp(
        NAME VARCHAR(20),        #名字
        OWNER VARCHAR(20),       #主人
        species VARCHAR(20),     #种类
        sex CHAR(1),             #性别
        birth DATE,              #出生日期
        death DATE               #死亡日期
);
INSERT INTO temp
VALUES
('张三','靠','人类','男',NOW(),NOW());

SELECT * FROM temp;
