O que � o Presley (Recommender System for Distributed Software Teams)
----------------------------------------------------------------------

O Presley � uma ferramenta de comunica��o ass�ncrona que favorece a colabora��o entre grupos de desenvolvimento
fisicamente distribu�dos e tem o objetivo de diminuir as defici�ncias geradas pela fraca identifica��o dos peritos do
projeto, isso a partir da sele��o das pessoas mais qualificadas para a resolu��o de problemas gerados durante a fase 
implementa��o.

Requisitos de software necessarios
-----------------------------------------------------------------------
JDK(Java Development Kit) em ambiente Windows com vers�o jdk1.6.0_07 ou superior
Eclipse SDK vers�o 3.4.1
Banco de Dados MySQL vers�o 5.0 (Obs: Ao instalar-lo deixa a senha do root vazia)
Subversion Client vers�o 1.5.5


Como preparar o ambiente
------------------------
Instale os softwares litados acima e siga os seguintes passos:

1� Instalar o subclipse
   Abra o eclipse e siga as instru��es no site http://subclipse.tigris.org/install.html
        Obs 1: Na janela "New Update Site" mostrada no site digite http://subclipse.tigris.org/update_1.4.x no campo URL
        Obs 2: � necessario al�m da biblioteca Subclipse as bibliotecas JavaHL ou SVNKit para adcionar o repositorio

2� Baixar os fontes do projeto
   Crie um diretorio no seu computador e em seguida execute o Prompt de comando do windows com o comando
   "svn checkout https://presley.googlecode.com/svn/trunk/"

3� Criar o banco de dados no MySQL
   Execute o Script de Cria��o existente em /presley-server/src/main/resources/sql/ScriptPresley2.sql

4� Importar os fontes para o eclipse
   Abra o Eclipse em um novo Workspace e importe os tr�s projetos. Em File -> Import -> Existing Projects into Workspace, 
   aponte para trunk\code e selecione os tr�s projetos.

5� Criar o presley-common.jar
   Dentro do eclipse no projeto presley-common click com o bot�o direito em src/main/resources/presley-commons.jardesc
   and select "Create JAR". Ser� criado o presley-common.jar no diretorio presley-common do seu workspace, copie-o para
   /presley-client/src/main/resources

Importing source code into Eclipse
----------------------------------

File -> Import -> Existing Projects into Workspace

* Select root directory: path/to/presley/code
* Select all projects


Creating presley-common.jar
---------------------------

In Eclipse project presley-common, right click in src/main/resources/presley-commons.jardesc and select "Create JAR"