# Recipe App

[![<recipe app>](https://circleci.com/gh/yonatankarp/recipe-app.svg?style=shield)](https://circleci.com/gh/yonatankarp/recipe-app/?branch=main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_recipe-app&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=yonatankarp_recipe-app)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_recipe-app&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=yonatankarp_recipe-app)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_recipe-app&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=yonatankarp_recipe-app)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_recipe-app&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=yonatankarp_recipe-app)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=yonatankarp_recipe-app&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=yonatankarp_recipe-app)

This is a recipe web application based on Spring Boot, Thymeleaf and Hibernate.

## Usage

You can run the application via maven by running the following command on your
terminal:

```shell
./mvnw spring-boot:run
```

and then access it on your browser on the address `localhost:8080`

By default, the application will contain 2 recipes.

## Data Models

![](./docs/resources/data-models.png)
