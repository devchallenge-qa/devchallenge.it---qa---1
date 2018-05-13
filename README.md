## Requirements
* Java 8
* Maven 3.5.3

##Running tests
```
1. git clone https://github.com/devchallenge-qa/devchallenge.it---qa---1.git
2. cd devchallenge.it---qa---1
4. mvn clean test
```

##Running separate test suites
1. To run tests for /pet use: **mvn clean -Dtest=PetControllerTest test**
2. To run tests for /store use: **mvn clean -Dtest=PetStoreControllerTest test**
