# Agronowtest
Repositório criado para projeto teste da Agronow.
A aplicação é capaz de realizar o CRUD de bares através dos endpoints mostrados abaixo, e também é capaz de sortear um bar e mostrar a distância, valor de corridas de uber e duração da corrida do bar sorteado até o escritório da Agronow escolhido (São Paulo ou São José).
O deploy é feito pelo Apache Tomcat.

## Pré-requisitos
É necessário ter instalado Apache Tomcat, PostgreSQL e Apache Maven.

### Setup
Deploy:
O deploy é realizado através do Apache Tomcat.
Para realizar o deploy, basta copiar o arquivo agronowtest-1.0.0.war e colar na pasta webapps do Apache Tomcat.
O próximo passo é iniciar o Tomcat. Para isso, basta acessar a pasta bin e executar o arquivo startup.bat.
Pronto, o deploy pelo Apache Tomcat está feito e agora é possível realizar as requisições.

### Utilização da aplicação
A documentação da API pode ser acessada via Swagger UI, pelo link http://localhost:8080/agronowtest-1.0.0/swagger-ui.html, uma vez que a aplicação esteja rodando.

#### Operações C.R.U.D.
- Para adicionar um novo bar, basta enviar uma requisição do tipo POST na URL http://localhost:8080/agronowtest-1.0.0/bar, com o seguinte corpo:
```
{
    "name": "bar-name",
    "address": "bar-address",
    "coordinates": "lat, long"
}
```
- Para buscar todos os bares cadastrados, basta enviar uma requisição do tipo GET na URL http://localhost:8080/agronowtest-1.0.0/bar;

- Para buscar um bar com um ID específico, basta enviar uma requisição do tipo GET na URL http://localhost:8080/agronowtest-1.0.0/bar/{barId}, onde barId é o ID do bar que deverá ser buscado;

- Para atualizar um bar, é necessário enviar uma requisição do tipo PUT na URL http://localhost:8080/agronowtest-1.0.0/bar/{barId}, onde barId é o ID do bar que será atualizado. É necessário enviar o seguinte corpo na requisição:
```
{
    "name": "bar-name-updated",
    "address": "bar-address-updated",
    "coordinates": "lat, long (updated)"
}
```
- Para deletar um bar do banco de dados, basta enviar uma requisição do tipo DELETE para a URL http://localhost:8080/agronowtest-1.0.0/bar/{barId}, onde barId é o ID do bar que deverá ser removido.

- (Funcionalidade em desenvolvimento) Para realizar sorteio do bar e ter as estimativas de valor e distância, é necessário enviar uma requisição do tipo POST para a URL http://localhost:8080/agronowtest-1.0.0/raffle/{officeId}, onde officeId é o id do escritório que será utilizado como referência de ponto inicial para a estimativa das corridas de Uber.

## Feito com

* [Spring-boot](https://spring.io/guides/gs/spring-boot/)
* [Maven](https://maven.apache.org/) 
* [PostgreSQL] (https://www.postgresql.org/)
* [Uber API](https://developer.uber.com/)
* [Swagger] (https://swagger.io/)  
## Autor
Pablo Vilela Bochi
