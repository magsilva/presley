CREATE DATABASE IF NOT EXISTS presley;

use presley;

CREATE TABLE  projeto (
  nome varchar(40) NOT NULL,
  ativo tinyint(1) default NULL,
  endereco_Log text NOT NULL,
  endereco_Servidor_Gravacao text NOT NULL,
  diretorio_Subversion varchar(45) NOT NULL,
  endereco_Servidor_Projeto text NOT NULL,
  PRIMARY KEY  (nome)
);


CREATE TABLE  conhecimento (
  nome varchar(70) NOT NULL,
  descricao varchar(150) default NULL,
  PRIMARY KEY  (nome)
);


CREATE TABLE  arquivo (
  id int(10) unsigned NOT NULL auto_increment,
  arquivo_nome varchar(70) NOT NULL,
  endereco_servidor text NOT NULL,
  quantidadePalavras int(11) default NULL,
  endereco_log text NOT NULL,
  PRIMARY KEY  (id)
);

CREATE TABLE  conhecimento_has_arquivo (
  arquivo_id int(10) unsigned NOT NULL,
  conhecimento_nome varchar(70) NOT NULL,
  PRIMARY KEY  (arquivo_id,conhecimento_nome),
  KEY conhecimento_has_arquivo_FKIndex1 (arquivo_id),
  KEY conhecimento_nome (conhecimento_nome),
  CONSTRAINT conhecimento_has_arquivo_ibfk_1 FOREIGN KEY (conhecimento_nome) REFERENCES conhecimento (nome) ON DELETE CASCADE,
  CONSTRAINT conhecimento_has_arquivo_ibfk_2 FOREIGN KEY (arquivo_id) REFERENCES arquivo (id) ON DELETE CASCADE
);

CREATE TABLE  palavra (
  id int(10) unsigned NOT NULL auto_increment,
  palavra varchar(50) NOT NULL,
  PRIMARY KEY  (id)
);


CREATE TABLE  arquivo_has_palavras (
  arquivo_id int(10) unsigned NOT NULL,
  palavra_id int(10) unsigned NOT NULL,
  quantidade int(10) unsigned NOT NULL,
  PRIMARY KEY  (arquivo_id,palavra_id),
  KEY arquivo_has_palavras (arquivo_id),
  KEY palavra_id (palavra_id),
  CONSTRAINT arquivo_has_palavras_ibfk_1 FOREIGN KEY (arquivo_id) REFERENCES arquivo (id) ON DELETE CASCADE,
  CONSTRAINT arquivo_has_palavras_ibfk_2 FOREIGN KEY (palavra_id) REFERENCES palavra (id) ON DELETE CASCADE
);


CREATE TABLE  desenvolvedor (
  email varchar(50) NOT NULL,
  nome varchar(100) default NULL,
  cvsNome varchar(50) default NULL,
  senha varchar(40) NOT NULL,
  listaEmail text,
  PRIMARY KEY  (email)
);


CREATE TABLE  problema (
  id int(10) unsigned NOT NULL auto_increment,
  desenvolvedor_email varchar(50) NOT NULL,
  atividade_id int(11) default NULL,
  descricao text,
  resolvido tinyint(1) default NULL,
  dataRelato date default NULL,
  mensagem longtext,
  conhecimento_nome varchar(70) default NULL,
  projeto_nome varchar(40) NOT NULL,
  PRIMARY KEY  (id),
  KEY problema_FKIndex1 (atividade_id),
  KEY conhecimento_nome (conhecimento_nome),
  KEY projeto_nome (projeto_nome),
  KEY FK_desenvolvedor (desenvolvedor_email),
  CONSTRAINT FK_desenvolvedor FOREIGN KEY (desenvolvedor_email) REFERENCES desenvolvedor (email),
  CONSTRAINT problema_ibfk_1 FOREIGN KEY (conhecimento_nome) REFERENCES conhecimento (nome) ON DELETE CASCADE
);


CREATE TABLE  problema_has_classe (
  problema_id int(10) unsigned NOT NULL,
  arquivo_id int(10) unsigned NOT NULL,
  classe varchar(200) NOT NULL,
  PRIMARY KEY  (problema_id,arquivo_id,classe),
  KEY arquivo_id (arquivo_id),
  CONSTRAINT problema_has_classe_ibfk_1 FOREIGN KEY (problema_id) REFERENCES problema (id) ON DELETE CASCADE,
  CONSTRAINT problema_has_classe_ibfk_2 FOREIGN KEY (arquivo_id) REFERENCES arquivo (id) ON DELETE CASCADE
);

CREATE TABLE  solucao (
  id int(10) unsigned NOT NULL auto_increment,
  desenvolvedor_email varchar(50) NOT NULL,
  problema_id int(10) unsigned NOT NULL,
  resolveu tinyint(1) default NULL,
  dataProposta date default NULL,
  mensagem longtext,
  retornoSolucao text,
  id_solucaoResposta int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (id),
  KEY solucao_FKIndex1 (problema_id),
  KEY desenvolvedor_email (desenvolvedor_email),
  CONSTRAINT FK_Solucao_Problema FOREIGN KEY (problema_id) REFERENCES problema (id) ON DELETE CASCADE,
  CONSTRAINT solucao_ibfk_1 FOREIGN KEY (desenvolvedor_email) REFERENCES desenvolvedor (email) ON DELETE CASCADE
);

CREATE TABLE  mensagem (
  id int(10) unsigned NOT NULL auto_increment,
  desenvolvedor_destino_email varchar(50) NOT NULL,
  problema_id int(10) unsigned NOT NULL,
  PRIMARY KEY  (id),
  KEY problema_id (problema_id),
  KEY desenvolvedor_destino_email (desenvolvedor_destino_email),
  CONSTRAINT mensagem_ibfk_1 FOREIGN KEY (problema_id) REFERENCES problema (id) ON DELETE CASCADE,
  CONSTRAINT mensagem_ibfk_2 FOREIGN KEY (desenvolvedor_destino_email) REFERENCES desenvolvedor (email) ON DELETE CASCADE
);

CREATE TABLE  conhecimento_pai_filho (
  conhecimento_pai_nome varchar(70) NOT NULL,
  conhecimento_filho_nome varchar(70) NOT NULL,
  PRIMARY KEY  (conhecimento_pai_nome,conhecimento_filho_nome),
  KEY conhecimento_filho_nome (conhecimento_filho_nome),
  CONSTRAINT conhecimento_pai_filho_ibfk_1 FOREIGN KEY (conhecimento_pai_nome) REFERENCES conhecimento (nome) ON DELETE CASCADE,
  CONSTRAINT conhecimento_pai_filho_ibfk_2 FOREIGN KEY (conhecimento_filho_nome) REFERENCES conhecimento (nome) ON DELETE CASCADE
);


CREATE TABLE  desenvolvedor_has_conhecimento (
  desenvolvedor_email varchar(50) NOT NULL,
  conhecimento_nome varchar(70) NOT NULL,
  grau double NOT NULL,
  PRIMARY KEY  (desenvolvedor_email,conhecimento_nome),
  KEY conhecimento_nome (conhecimento_nome),
  CONSTRAINT desenvolvedor_has_conhecimento_ibfk_1 FOREIGN KEY (desenvolvedor_email) REFERENCES desenvolvedor (email) ON DELETE CASCADE,
  CONSTRAINT desenvolvedor_has_conhecimento_ibfk_2 FOREIGN KEY (conhecimento_nome) REFERENCES conhecimento (nome) ON DELETE CASCADE
);

CREATE TABLE  log_controle_versao (
  idLog int(10) unsigned NOT NULL auto_increment,
  desenvolvedor_email varchar(50) NOT NULL,
  arquivo_id int(10) unsigned NOT NULL,
  data_hora datetime NOT NULL,
  PRIMARY KEY  (idLog),
  KEY FK_log_controle_versao_2 (desenvolvedor_email),
  KEY FK_log_controle_versao_1 (arquivo_id),
  CONSTRAINT FK_log_controle_versao_1 FOREIGN KEY (arquivo_id) REFERENCES arquivo (id) ON DELETE CASCADE,
  CONSTRAINT FK_log_controle_versao_2 FOREIGN KEY (desenvolvedor_email) REFERENCES desenvolvedor (email) ON DELETE CASCADE
);