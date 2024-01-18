CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role INT NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT role_check CHECK (role >= 0 AND role <= 2)
);
