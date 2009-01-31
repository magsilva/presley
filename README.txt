O que é o Presley (Recommender System for Distributed Software Teams)
----------------------------------------------------------------------

O Presley é uma ferramenta de comunicação assíncrona que favorece a colaboração entre grupos de desenvolvimento
fisicamente distribuídos e tem o objetivo de diminuir as deficiências geradas pela fraca identificação dos peritos do
projeto, isso a partir da seleção das pessoas mais qualificadas para a resolução de problemas gerados durante a fase 
implementação.

Requisitos de software necessarios
-----------------------------------------------------------------------
JDK(Java Development Kit) em ambiente Windows com versão jdk1.6.0_07 ou superior
Eclipse SDK versão 3.4.1
Banco de Dados MySQL versão 5.0 (Obs: Ao instalar-lo deixa a senha do root vazia)
Subversion Client versão 1.5.5


Como preparar o ambiente
------------------------
Instale os softwares litados acima e siga os seguintes passos:

1º Instalar o subclipse
   Abra o eclipse e siga as instruções no site http://subclipse.tigris.org/install.html
        Obs 1: Na janela "New Update Site" mostrada no site digite http://subclipse.tigris.org/update_1.4.x no campo URL
        Obs 2: É necessario além da biblioteca Subclipse as bibliotecas JavaHL ou SVNKit para adcionar o repositorio

2º Baixar os fontes do projeto
   Crie um diretorio no seu computador e em seguida execute o Prompt de comando do windows com o comando
   "svn checkout https://presley.googlecode.com/svn/trunk/"

3º Criar o banco de dados no MySQL
   Execute o Script de Criação existente em /presley-server/src/main/resources/sql/ScriptPresley2.sql

4º Importar os fontes para o eclipse
   Abra o Eclipse em um novo Workspace e importe os três projetos. Em File -> Import -> Existing Projects into Workspace, 
   aponte para trunk\code e selecione os três projetos.

5º Criar o presley-common.jar
   Dentro do eclipse no projeto presley-common click com o botão direito em src/main/resources/presley-commons.jardesc
   and select "Create JAR". Será criado o presley-common.jar no diretorio presley-common do seu workspace, copie-o para
   /presley-client/src/main/resources

Importing source code into Eclipse
----------------------------------

File -> Import -> Existing Projects into Workspace

* Select root directory: path/to/presley/code
* Select all projects


Creating presley-common.jar
---------------------------

In Eclipse project presley-common, right click in src/main/resources/presley-commons.jardesc and select "Create JAR"