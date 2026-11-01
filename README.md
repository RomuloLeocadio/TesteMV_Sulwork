# Eventos de Alimentação

Projeto do desafio da Sulwork: um sistema para organizar eventos de alimentação, onde os participantes combinam o que levar e dá para ver o que ainda falta.

## Aplicação publicada

- Sistema: https://testemv-sulwork.onrender.com/
- Swagger: https://testemv-sulwork.onrender.com/swagger-ui/index.html

Publiquei no Render (aplicação em Docker) com o banco PostgreSQL do próprio Render, ambos no plano gratuito. Por isso duas observações:

- A aplicação "dorme" depois de 15 minutos sem acesso, então a primeira abertura pode levar cerca de 1 minuto.
- O banco gratuito do Render expira em 01/12/2026. Depois dessa data o sistema deixa de funcionar a menos que o banco seja recriado (basta trocar as variáveis de ambiente SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME e SPRING_DATASOURCE_PASSWORD).

## O que usei

- Java 21 + Spring Boot (API REST, JPA/Hibernate)
- PostgreSQL
- Swagger (springdoc) para documentar a API
- Frontend em HTML, CSS e JavaScript simples
- JUnit 5 e Mockito nos testes
- Docker Compose

## Como o projeto está organizado

O backend tem as pastas controller (recebe as requisições), service (regras de negócio), repositories (banco), model (entidades), dto e exception (erros de negócio e o GlobalExceptionHandler, que devolve a mensagem certa com o status HTTP certo).
O frontend está em src/main/resources/static e é servido pelo próprio Spring.

## Como rodar

bash
docker compose up --build


- Sistema: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui/index.html

Se quiser rodar só o banco no Docker e a aplicação pelo Maven:

bash
docker compose up -d postgres
mvn spring-boot:run


## Como rodar os testes

Com o banco ligado:

bash
mvn test

Testei as transições de status do evento, a validação de CPF e a regra de quantidade máxima nas contribuições.

## Decisões que tomei

O desafio deixa algumas regras em aberto, então escolhi assim:

**Status do evento:**
- RASCUNHO pode ir para ABERTO ou CANCELADO
- ABERTO pode ir para ENCERRADO ou CANCELADO
- ENCERRADO pode ir para REALIZADO ou CANCELADO
- REALIZADO e CANCELADO são finais

**Prazo e status:** para contribuir (incluir, alterar ou remover) o evento precisa estar ABERTO **e** o prazo ainda não pode ter passado. Categorias e itens só mudam com o evento em RASCUNHO ou ABERTO.
**Participantes:** o CPF é validado pelos dígitos verificadores e é único. O mesmo participante pode estar em vários eventos, mas só uma vez em cada. Só quem está inscrito pode contribuir.
**Contribuições:** várias pessoas podem contribuir com o mesmo item, mas a soma não pode passar da quantidade necessária (a API avisa quanto ainda falta).

## Como calculei a cobertura

- É uma regra de três: unidades contribuídas ÷ unidades necessárias × 100, por categoria e para o evento todo.
- A meta da categoria é o total de unidades que eu quero nela. Ela aparece separada, dizendo se foi atingida ou não.
- Itens obrigatórios que ainda não foram completados aparecem como pendências.
- A soma das contribuições de cada item é feita com uma consulta SQL nativa (SUM na tabela de contribuições).

Endpoint: GET /api/eventos/{id}/cobertura

## Feature livre: alerta de alérgenos

**Problema:** quem organiza o evento não sabia se algum item podia fazer mal para algum participante.
**Como funciona:** cada restrição alimentar do participante (ex.: intolerante à lactose) aponta para os alérgenos que ele não pode consumir, e cada item também tem seus alérgenos. O sistema compara os dois e mostra uma tabela com o item e as pessoas afetadas, só entre os inscritos no evento. É só um aviso, não bloqueia nada.
Endpoint: GET /api/eventos/{id}/alertas-alergenos

## Uso de IA

Usei o Claude (Anthropic) durante o desenvolvimento, para modelagem, regras de negócio, escrita dos services e controllers, consulta nativa, frontend, Swagger, testes e para entender erros (principalmente do Docker e da conexão com o banco).
**Sugestão que aceitei:** o GlobalExceptionHandler, que centraliza o tratamento de erros.
