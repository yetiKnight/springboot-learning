-- 初始化数据脚本
-- 面试重点：MyBatis Plus测试数据

INSERT INTO users (username, password, email, first_name, last_name, status) VALUES
('admin', 'admin123', 'admin@example.com', 'Admin', 'User', 'ACTIVE'),
('user1', 'user123', 'user1@example.com', 'John', 'Doe', 'ACTIVE'),
('user2', 'user123', 'user2@example.com', 'Jane', 'Smith', 'ACTIVE'),
('user3', 'user123', 'user3@example.com', 'Bob', 'Johnson', 'INACTIVE'),
('test', 'test123', 'test@example.com', 'Test', 'User', 'ACTIVE');
