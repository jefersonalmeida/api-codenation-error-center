[![CI Status](https://github.com/jefersonalmeida/api-codenation-error-center/workflows/CI/badge.svg)](https://github.com/jefersonalmeida/api-codenation-error-center)

# Codenation - Central de Erros
Projeto prático do AceleraDev Java Online da CodeNation

## Author

- **Jeferson Almeida**
    - Github - [jefersonalmeida](https://github.com/jefersonalmeida)
    - Linkedin - [jefersonalmeida](https://www.linkedin.com/in/jefersonalmeida/)
    - Twitter - [____jeferson](https://twitter.com/____jeferson)
    - Email - [me@jeferson.net.br](mailto://me@jeferson.net.br)

## Apoiadores

[CI&T](https://br.ciandt.com/), [Conta Azul](https://contaazul.com.br), [GuiaBolso](https://guiabolso.com.br), [PagueVeloz](https://www.pagueveloz.com.br/), [Rocket.Chat](https://rocket.chat), [Sanar](https://www.sanarmed.com/), [Nexfar](https://nexfar.com.br/#/), [Senior](https://www.senior.com.br/), [Voyager](https://www.voyagerportal.com/company/) e [Itaú](https://itau.com.br)

### Objetivo
Em projetos modernos é cada vez mais comum o uso de arquiteturas baseadas em serviços ou microsserviços. Nestes ambientes complexos, erros podem surgir em diferentes camadas da aplicação (backend, frontend, mobile, desktop) e mesmo em serviços distintos. Desta forma, é muito importante que os desenvolvedores possam centralizar todos os registros de erros em um local, de onde podem monitorar e tomar decisões mais acertadas. Neste projeto vamos implementar uma API Rest para centralizar registros de erros de aplicações.

Abaixo estão os requisitos desta API, o time terá total liberdade para tomar as decisões técnicas e de arquitetura da API, desde que atendam os requisitos abaixo.

### API

- [Backend DOCS](https://codenation-error-center.herokuapp.com/swagger-ui.html)
- [Frontend DEMO](https://codenation-java.jeferson.net.br)

### Tecnologia
- Java *(8)*
- Spring boot
- Gradle
- H2 *(Testes)*
- PostgreSQL *(Produção)*
- JWT
- GitHub
- Springdoc
- Heroku

**Premissas**;

* A API deve ser pensada para atender diretamente um front-end
* Deve ser capaz de gravar os logs de erro em um banco de dados relacional
* O acesso a ela deve ser permitido apenas por requisições que utilizem um token de acesso válido &nbsp;

**Funcionalidades**;
* Deve permitir a autenticação do sistema que deseja utilizar a API gerando o Token de Acesso
* Pode ser acessado por multiplos sistemas
* Deve permitir gravar registros de eventos de log salvando informações de Level(error, warning, info), Descrição do Evento, LOG do Evento, ORIGEM(Sistema ou Serviço que originou o evento), DATA(Data do evento), QUANTIDADE(Quantidade de Eventos de mesmo tipo)
* Deve permitir a listagem dos eventos juntamente com a filtragem de eventos por qualquer parâmetro especificado acima
* Deve suportar Paginação
* Deve suportar Ordenação por diferentes tipos de atributos
* A consulta de listagem não deve retornar os LOGs dos Eventos
* Deve permitir a busca de um evento por um ID, dessa maneira exibindo o LOG desse evento em específico
