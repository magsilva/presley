create database presley;

use presley;

CREATE TABLE conhecimento (
  nome VARCHAR(40) NOT NULL,
  descricao VARCHAR(150) NULL,
  PRIMARY KEY(nome)
);

CREATE TABLE desenvolvedor (
  email VARCHAR(40) NOT NULL,
  nome VARCHAR(40) NULL,
  localidade VARCHAR(50) NULL,
  PRIMARY KEY(email)
);

CREATE TABLE atividade (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  desenvolvedor_email VARCHAR(40) NOT NULL,
  gerente_email VARCHAR(40) NOT NULL,
  atividadePai INTEGER UNSIGNED NULL,
  descricao VARCHAR(120) NULL,
  dataInicio DATE NULL,
  dataFim DATE NULL,
  terminada BOOL NULL,
  PRIMARY KEY(id),
  FOREIGN KEY (desenvolvedor_email) REFERENCES desenvolvedor (email),
  FOREIGN KEY (gerente_email) REFERENCES desenvolvedor (email),
  INDEX atividade_FKIndex3(atividadePai)
);

CREATE TABLE atividade_has_conhecimento (
  atividade_id INTEGER UNSIGNED NOT NULL,
  conhecimento_nome VARCHAR(40) NOT NULL,
  PRIMARY KEY(atividade_id, conhecimento_nome),
  INDEX atividade_has_conhecimento_FKIndex1(atividade_id),
  FOREIGN KEY (conhecimento_nome) REFERENCES conhecimento (nome)  
);

CREATE TABLE desenvolvedor_has_conhecimento (
  desenvolvedor_email VARCHAR(40) NOT NULL,
  conhecimento_nome VARCHAR(40) NOT NULL,
  PRIMARY KEY(desenvolvedor_email, conhecimento_nome),
  FOREIGN KEY (desenvolvedor_email) REFERENCES desenvolvedor (email), 
  FOREIGN KEY (conhecimento_nome) REFERENCES conhecimento (nome) 
  

);

CREATE TABLE problema (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  atividade_id INTEGER UNSIGNED NOT NULL,
  descricao VARCHAR(100) NULL,
  resolvido BOOL NULL,
  dataRelato DATE NULL,
  mensagem VARCHAR(250) NULL,
  PRIMARY KEY(id),
  INDEX problema_FKIndex1(atividade_id)
);

CREATE TABLE solucao (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  desenvolvedor_email VARCHAR(40) NOT NULL,
  problema_id INTEGER UNSIGNED NOT NULL,
  resolveu BOOL NULL,
  dataProposta DATE NULL,
  mensagem VARCHAR(250) NULL,
  PRIMARY KEY(id),
  INDEX solucao_FKIndex1(problema_id),
  FOREIGN KEY (desenvolvedor_email) REFERENCES desenvolvedor (email)  
);

