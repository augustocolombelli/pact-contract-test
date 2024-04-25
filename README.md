<<<<<<< HEAD
# Pact Contract Test
This repository presents a use case of contract testing employing [Pact](https://docs.pact.io/) tests. To facilitate contract testing, three microservices were developed to assess integration: `book-sales-service`, `person-data-service`, and `book-data-service`."

- The `person-data-service` has an API that receives the person's code and returns their data.
- The `book-data-service`has two APIs: one that receives the book code and returns its data, and another responsible for updating the stock.
- The main service is `book-sales-service`. This service is responsible for carrying out the sales operation, orchestrating integration with other microservices.

![1-container-diagram.png](docs%2Fimages%2F1-container-diagram.png)

All microservices are located in the `microservices` folder. Each of them was created using Java, Spring Boot, and Gradle. Considering the purpose of this project, which is contract testing, there is no integration with a database; all the data are stored in an in-memory collection.

## Getting started
There are two ways to run the services locally: 
- Using `docker-compose` to create all the containers used in the application; 
- Starting each service in our machine with gradle.

. If you want to debug the code and run some specific test, it's better to run the service without a container. Below more details for each approach.

### Start services in the machine

### Start services with docker compose
There is an option to start all services using `docker-compose`. This approach is preferable to see the integration between all services in a `black-box` context.

All services and the `pack broker` are included in the file `docker-compose.yml`.


Run the command below to start all containers:
> docker-compose up -d

#### Down the services
Run the command below to down the services:
> docker-compose down

Run the commands below to remove the images:
```
docker rmi book-sales-service:v1
docker rmi person-data-service:v1
docker rmi book-data-service:v1 
```

### Testing 

#### Person data service
Make a request to get persons:
> curl --location --request GET 'http://localhost:8082/persons'
> curl --location --request GET 'http://localhost:8082/persons/1001'

#### Book data service
##### Get books
Make a request to get persons:
> curl --location --request GET 'http://localhost:8083/books'
> curl --location --request GET 'http://localhost:8083/books/201'

##### Update the Stock
Make a request to update the stock:
```
curl --location 'http://localhost:8083/books/updateStock' \
--header 'Content-Type: application/json' \
--data '{"id": 202,"quantity": 1}'
```

Try to update stock without quantity:
```
curl --location 'http://localhost:8083/books/updateStock' \
--header 'Content-Type: application/json' \
--data '{"id": 202}'
```

Try to update stock without id and quantity:
```
curl --location 'http://localhost:8083/books/updateStock' \
--header 'Content-Type: application/json' \
--data '{}'
```

#### Book sales service
Make a request to sale a book:
```
curl --location 'http://localhost:8081/book-sales' \
--header 'Content-Type: application/json' \
--data '{
"personId": 1001,
"bookId": 201
}'
```

Try to make a request without values:
```
curl --location 'http://localhost:8080/book-sales' \                                                                                                                                ok 
--header 'Content-Type: application/json' \
--data '{}'
```

### Contract Test
Consumer (book-sales-service)
// Clean project
> ./gradlew clean
// Build, then will run the test and create the Json with the pact
> ./gradlew build
// Publish the contract to broker
> ./gradlew pactPublish

Provider:
// Clean the project
> ./gradlew clean
// Run the test and check the contract
> ./gradlew build

## TODO
### Article
- OK - Finish the topic - Verificação do contrato pelos providers
- OK - Finish the topic - Simulação 3: Provider altera nome do campo para requisição de POST
- OK - Change all branches name - simulation-1-blababla
- OK - Review all images;
- OK - Review all code;
- OK - Refine the text;
- OK - Second refinement;
- Verify all files and create a new project with only one commit - Not forget branches - Create subproject
- Add to readme the steps to start the application without docker 
- Add to readme the steps necessary to run the providers/consumers test
- Add to readme the steps necessary to run the services with docker-compose
- Add to readme how it's possible to test all API
- Cover all code with unit tests;
- Final test without Docker;
- Final test with Docker;
- Third and last refinement;
- Publish my Github;
- Translate to English;
- Publish to Medium (English)
- Publish to TW (English and Portuguese)
