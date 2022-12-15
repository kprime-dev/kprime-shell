# README

## BUILD
> mvn clean package

## RUN
* In local environment on default port 4567:
> java -jar target/artifact.jar
* Try in browser 
> http://localhost:4567/hello

you sould see:

> Hello World

## DEPLOY
* This artifact is builded with name 'artifact'
* It is configured to deploy on heroku.
* Change artifact name in 'pom.xml' and 'Procfile'
* It is configured to run on Java 8.
* Change version in 'pom.xml' and 'system.properties'

## LICENSE