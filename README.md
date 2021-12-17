# api-cervejaria

<h2>API com java Spring Boot. Projeto desenvolvido junto ao bootcamp da Spread na DIO(Digital Inovation One)</h2>
<h3>Desenvolvimento de testes unitários para validar uma API REST de gerenciamento de estoques de cerveja.</h3>

Uma API REST para o gerenciamento de estoques de cerveja. com testes unitários para validar o nosso sistema de gerenciamento de estoques de cerveja e criação testes unitários com JUnit e Mockito. Tudo isso com a prática do TDD.

* Foco nos testes unitários: o quão é importante o desenvolvimento de  testes como parte do ciclo de desenvolvimento de software.
* Principais frameworks para testes unitários em Java: JUnit, Mockito e Hamcrest. 
* Desenvolvimento de testes unitários para validação de funcionalides de: criação, listagem, consulta por nome e exclusão de cervejas.
* TDD: em incremento e decremento do número de cervejas no estoque.

Para executar o projeto no terminal, digite o seguinte comando:

```shell script
mvn spring-boot:run 
```

Para executar a suíte de testes desenvolvida durante a live coding, basta executar o seguinte comando:

```shell script
mvn clean test
```

Após executar o comando acima, basta apenas abrir o seguinte endereço e visualizar a execução do projeto:

```
http://localhost:8080/api/v1/cervejas
```
