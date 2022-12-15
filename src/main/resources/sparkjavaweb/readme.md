# README

## BUILD
> mvn clean package

## RUN
* In local environment on default port 8080:
> java -jar target/artifact.jar

* Try in browser:
> http://localhost:8080/hello
you sould see:
> Hello World

* Try in browser:
> http://localhost:8080/books/inventory
you sould see:
Welcome!
Name
Nome della Rosa
Guerra e pace

## DEPLOY
* This artifact is builded with name 'artifact'
* It is configured to deploy on heroku.
* Change artifact name in 'pom.xml' and 'Procfile'
* It is configured to run on Java 8.
* Change version in 'pom.xml' and 'system.properties'

## LICENSE