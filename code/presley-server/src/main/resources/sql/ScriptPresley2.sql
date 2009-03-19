create database presley;

use presley;


CREATE TABLE projeto (
  nome VARCHAR(40) NOT NULL,
  ativo BOOL NULL,
  endereco_Servidor_Leitura VARCHAR(200) NOT NULL,
  endereco_Servidor_Gravacao VARCHAR(200) NOT NULL,
  PRIMARY KEY(nome)
);

CREATE TABLE conhecimento (
  nome VARCHAR(40) NOT NULL,
  descricao VARCHAR(150) NULL,
  PRIMARY KEY(nome)
);

CREATE TABLE arquivo(
  id INTEGER UNSIGNED NOT NULL auto_increment,
  arquivo_nome varchar(40) NOT NULL,
  endereco_servidor varchar(200) NOT NULL,
  quantidadePalavras INTEGER NULL,
  PRIMARY KEY  (id)
);

CREATE TABLE conhecimento_has_arquivo (
  arquivo_id INTEGER UNSIGNED NOT NULL,
  conhecimento_nome VARCHAR(40) NOT NULL,
  PRIMARY KEY(arquivo_id, conhecimento_nome),
  INDEX conhecimento_has_arquivo_FKIndex1(arquivo_id),
  FOREIGN KEY (conhecimento_nome) REFERENCES conhecimento (nome) ON DELETE CASCADE,
  FOREIGN KEY (arquivo_id) REFERENCES arquivo (id) ON DELETE CASCADE
);


CREATE TABLE arquivo_has_palavras (
  arquivo_id INTEGER UNSIGNED NOT NULL,
  palavra VARCHAR(40) NOT NULL,
  quantidade INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY(arquivo_id, palavra),
  INDEX arquivo_has_palavras(arquivo_id),
  FOREIGN KEY (arquivo_id) REFERENCES arquivo (id) ON DELETE CASCADE
);

CREATE TABLE desenvolvedor (
  email VARCHAR(40) NOT NULL,
  nome VARCHAR(40) NULL,
  localidade VARCHAR(50) NULL,
  senha VARCHAR(40) NOT NULL,
  PRIMARY KEY(email)
);

CREATE TABLE problema (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  desenvolvedor_email VARCHAR(40) NOT NULL,
  atividade_id INTEGER NULL,
  descricao VARCHAR(100) NULL,
  resolvido BOOL NULL,
  dataRelato DATE NULL,
  mensagem VARCHAR(250) NULL,
  conhecimento_nome VARCHAR(40),
  PRIMARY KEY(id),
  INDEX problema_FKIndex1(atividade_id),
  FOREIGN KEY (conhecimento_nome) REFERENCES conhecimento (nome) ON DELETE CASCADE
);


CREATE TABLE problema_has_classe (
  problema_id INTEGER UNSIGNED NOT NULL,
  arquivo_id INTEGER UNSIGNED NOT NULL,
  classe VARCHAR(100) NOT NULL,
  PRIMARY KEY(problema_id, arquivo_id, classe),
  FOREIGN KEY (problema_id) REFERENCES problema (id) ON DELETE CASCADE,
  FOREIGN KEY (arquivo_id) REFERENCES arquivo (id) ON DELETE CASCADE
);


CREATE TABLE solucao (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  desenvolvedor_email VARCHAR(40) NOT NULL,
  problema_id INTEGER UNSIGNED NOT NULL,
  resolveu BOOL NULL,
  dataProposta DATE NULL,
  mensagem VARCHAR(250) NULL,
  retornoSolucao VARCHAR(250) NULL,
  id_solucaoResposta INTEGER UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY(id),
  INDEX solucao_FKIndex1(problema_id),
  FOREIGN KEY (desenvolvedor_email) REFERENCES desenvolvedor (email) ON DELETE CASCADE
);

CREATE TABLE mensagem (
  id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  desenvolvedor_destino_email VARCHAR (40) NOT NULL,
  problema_id INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (problema_id) REFERENCES problema (id) ON DELETE CASCADE,
  FOREIGN KEY (desenvolvedor_destino_email) REFERENCES desenvolvedor (email) ON DELETE CASCADE
);

CREATE TABLE conhecimento_pai_filho (
  conhecimento_pai_nome VARCHAR(40) NOT NULL,
  conhecimento_filho_nome VARCHAR(40) NOT NULL,
  PRIMARY KEY(conhecimento_pai_nome, conhecimento_filho_nome),
  FOREIGN KEY (conhecimento_pai_nome) REFERENCES conhecimento (nome) ON DELETE CASCADE,
  FOREIGN KEY (conhecimento_filho_nome) REFERENCES conhecimento (nome) ON DELETE CASCADE
);

CREATE TABLE desenvolvedor_has_conhecimento (
  desenvolvedor_email VARCHAR(40) NOT NULL,
  conhecimento_nome VARCHAR(40) NOT NULL,
  grau real NOT NULL,
  PRIMARY KEY(desenvolvedor_email, conhecimento_nome),
  FOREIGN KEY (desenvolvedor_email) REFERENCES desenvolvedor (email) ON DELETE CASCADE, 
  FOREIGN KEY (conhecimento_nome) REFERENCES conhecimento (nome) ON DELETE CASCADE
);
