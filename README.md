# Agronowtest
Repositório criado para projeto teste da Agronow.

## Pré-requisitos
É necessário ter instalado Docker e Apache Tomcat.

### Setup
Deploy pelo Apache Tomcat:


Deploy pelo Docker:


### Utilização da aplicação
A documentação da API pode ser acessada via Swagger UI, pelo link http://localhost:8080/swagger-ui.html, uma vez que a aplicação esteja rodando.

#### Operações C.R.U.D.
- Para adicionar um novo bar, basta enviar uma requisição do tipo POST na URL http://localhost:8080/bar, com o seguinte corpo:
```
{
    "name": "bar-name",
    "address": "bar-address",
    "coordinates": "lat, long"
}
```
- Para buscar todos os bares cadastrados, basta enviar uma requisição do tipo GET na URL http://localhost:8080/bar;

- Para buscar um bar com um ID específico, basta enviar uma requisição do tipo GET na URL http://localhost:8080/bar/{barId}, onde barId é o ID do bar que deverá ser buscado;

- Para atualizar um bar, é necessário enviar uma requisição do tipo PUT na URL http://localhost:8080/bar/{barId}, onde barId é o ID do bar que será atualizado. É necessário enviar o seguinte corpo na requisição:
```
{
    "name": "bar-name-updated",
    "address": "bar-address-updated",
    "coordinates": "lat, long (updated)"
}
```
- Para deletar um bar do banco de dados, basta enviar uma requisição do tipo DELETE para a URL http://localhost:8080/bar/{barId}, onde barId é o ID do bar que deverá ser removido.

- (Funcionalidade em desenvolvimento) Para realizar sorteio do bar e ter as estimativas de valor e distância, é necessário enviar uma requisição do tipo POST para a URL http://localhost:8080/raffle, com o seguinte corpo:
```
{
    "officeId": <office-id>
}
```
## Feito com

* [Spring-boot](https://spring.io/guides/gs/spring-boot/)
* [Maven](https://maven.apache.org/) 
* [Uber API](https://developer.uber.com/)
  
## Autor
Pablo Vilela Bochi
