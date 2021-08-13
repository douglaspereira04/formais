# Linguagens Formais e Compiladores

Projeto Java8 utilizando o software de gerenciamento de projetos Maven


## Compilar aplicação
- Caso não tenha instalado o jdk8 instalado execute no terminal:
```
sudo apt install openjdk-8-jdk
```
- No diretorio raiz do projeto execute:
```
mvn clean package install
```

## Executar aplicação 
No diretorio raiz do projeto:
- Garanta que a aplicação tem permissão de excução executando o comando:
```
chmod +rwx target/GALS-0.5-PRIMEIRA-ENTREGA-jar-with-dependencies.jar
```
- Execute a aplicação com o comando:
```
java -jar target/GALS-0.5-PRIMEIRA-ENTREGA-jar-with-dependencies.jar 
```
