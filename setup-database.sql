-- Cria o banco de dados se ele não existir
CREATE DATABASE IF NOT EXISTS banco_cvetti_users
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Seleciona o banco de dados para usar
USE banco_cvetti_users;

-- Cria a tabela 'usuario'
CREATE TABLE IF NOT EXISTS usuario (
    id VARCHAR(36) NOT NULL PRIMARY KEY,    -- Para armazenar UUID como String
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    cpf VARCHAR(14) NOT NULL UNIQUE,        -- Formato XXX.XXX.XXX-XX
    senha VARCHAR(255) NOT NULL,            -- Hash da seha
    saldo DECIMAL(15, 2) DEFAULT 0.00,
    nascimento DATE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Opcional: Adicionar um usuário de exemplo (descomente se necessário)
-- INSERT INTO usuario (id, nome, email, cpf, senha, saldo, nascimento) VALUES
-- (UUID(), 'Usuário Teste', 'teste@example.com', '123.456.789-00', 'senha123', 100.50, '1990-01-01');

SELECT 'Script setup_database.sql executado.' AS status;