# 💻 Projeto Banco Fictício JavaFX  
**Projeto_LP_1Semestre**

Este é um sistema bancário fictício, desenvolvido em JavaFX para fins didáticos na disciplina de Linguagem de Programação. A aplicação possui uma interface gráfica amigável e se conecta a um banco de dados MySQL.

---

## ✅ Pré-requisitos

Certifique-se de ter os seguintes softwares instalados:

### ☕ Java Development Kit (JDK)
- **Versão:** 21 ou superior  
- **Download:**  
  - [Adoptium Temurin](https://adoptium.net/)  
  - [Oracle JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)  
- **Configuração:**  
  - Defina a variável de ambiente `JAVA_HOME`  
  - Adicione `JAVA_HOME/bin` ao `PATH`

### 🔧 Apache Maven
- **Versão:** 3.6.x ou superior  
- **Instalação:** [Guia oficial](https://maven.apache.org/install.html)  
- **Verifique a instalação:**
  ```bash
  mvn -v
  ```

### 🐬 MySQL Server
- **Versão recomendada:** 8.0 ou superior  
- **Download:** [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)

---

## ⚙️ Configuração do Banco de Dados

A aplicação usa o banco `banco_cvetti_users`, com uma tabela chamada `usuario`.

### 📂 Executando o script SQL

Use o script `setup_database.sql`, localizado na raiz do projeto.

#### MySQL Workbench
1. Abra o MySQL Workbench
2. Conecte-se ao servidor
3. Vá em `Arquivo > Abrir Script SQL` e selecione `setup_database.sql`
4. Execute com `Query > Execute (All or Selection)`

#### Linha de comando
```bash
mysql -u root -p < caminho/para/setup_database.sql
```

---

## 🔐 Configuração da Conexão com o Banco

Por padrão, a conexão está definida da seguinte forma:

- Servidor: `localhost`  
- Porta: `3306`  
- Banco: `banco_cvetti_users`  
- Usuário: `root`  
- Senha: `root`

Você pode:

### ✅ Usar root/root (modo rápido para testes)

Certifique-se de que o usuário `root` tem a senha `root`.

### 🔐 Criar um usuário específico (recomendado)

```sql
CREATE USER 'usuario_cvetti_app'@'localhost' IDENTIFIED BY 'sua_senha_segura';
GRANT ALL PRIVILEGES ON banco_cvetti_users.* TO 'usuario_cvetti_app'@'localhost';
FLUSH PRIVILEGES;
```

Depois, edite a classe `UserConnection.java` em:

```
src/main/java/com/example/demo/models/connection/UserConnection.java
```

---

## 🛠️ Compilação do Projeto

No terminal, na raiz do projeto (onde está o `pom.xml`):

```bash
mvn clean package
```

Um arquivo `.jar` será gerado na pasta `target/`, por exemplo:

```
target/Projeto_LP_1Semestre_Leonardo_Enzo_Gustavo_Gustavo_Arthur-0.0.1.jar
```

---

## ▶️ Executando a Aplicação

No terminal:

```bash
java -jar target/Projeto_LP_1Semestre_Leonardo_Enzo_Gustavo_Gustavo_Arthur-0.0.1.jar
```

A interface gráfica será exibida se tudo estiver corretamente configurado.

---

## 🐞 Problemas Comuns

### ❌ Erro de conexão com o banco
- Verifique se o MySQL Server está em execução.
- Confirme usuário, senha e nome do banco.

### ❌ Tabela `usuario` não existe
- Verifique se o script `setup_database.sql` foi executado corretamente.

### ❌ Dependências Maven não resolvidas
- Verifique se o Maven está instalado corretamente.
- Execute:
  ```bash
  mvn clean install
  ```

---

## 👨‍💻 Autores

- Leonardo  
- Enzo  
- Gustavo  
- Gustavo  
- Arthur
