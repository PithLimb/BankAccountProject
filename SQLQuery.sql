
CREATE DATABASE ATM;


USE ATM;
GO


CREATE TABLE BankAccounts (
    AccountID INT IDENTITY(10001,1) PRIMARY KEY,
    Password INT NOT NULL,
    Balance DECIMAL(10,2) NOT NULL,
    OwnerName VARCHAR(100) NOT NULL,
    Email VARCHAR(100) NULL,
    Phone VARCHAR(15) NULL
);

INSERT INTO BankAccounts (Password, Balance, OwnerName, Email, Phone) VALUES 
(11111, 100.00, 'Alice Johnson', 'alice.johnson@example.com', '555-1234'),
(22222, 250.50, 'Bob Smith', 'bob.smith@example.com', '555-5678'),
(33333, 500.00, 'Charlie Davis', 'charlie.davis@example.com', '555-8765'),
(44444, 750.25, 'Diana Prince', 'diana.prince@example.com', '555-4321'),
(55555, 1000.00, 'Ethan Hunt', 'ethan.hunt@example.com', '555-6789'),
(66666, 300.75, 'Fiona Gallagher', 'fiona.gallagher@example.com', '555-9876'),
(77777, 450.00, 'George Clooney', 'george.clooney@example.com', '555-5432'),
(88888, 600.50, 'Hannah Montana', 'hannah.montana@example.com', '555-2109'),
(99999, 800.00, 'Ian Somerhalder', 'ian.somerhalder@example.com', '555-3456'),
(100000, 950.00, 'Jenna Coleman', 'jenna.coleman@example.com', '555-6543');

SELECT * FROM BankAccounts;

