# MvnProjectManager Project

The purpose of MPM is to complement Apache Maven tool
simplifying common tasks, scaffolding opinionated frameworks.
 
## Usage

Define an environment variable KPRIME_HOME

define env.propeerties

It could be used as single command line:

    mpm <args>
    
Or it could be started in shell mode: 

    mpm

If the launch directory has a pom.xml file in it, the pom file will be read and will the project descriptor queried and manipulated;
otherwise you could create a brand-new pom.xml choosing one from available init-* commands. 

To list all mpm available commands, press enter, giving an empty command.


To end mpm-shell just type:
    
    quit

To print maven canonical layout:

    layout

## Semplified Tasks
* add a dependency commands:
    * mpm add-h2
    * mpm add-freemarker
    * mpm add-pdfbox
    * mpm add-junit4
    * mpm add-jetty
    * mpm add-sparkjava
* add site scafolding command:
    * mpm add-site
* set java version command:
    * mpm add-java8
* set fat-jar release command:
    * mpm add-fatjar-plugin
* list dependencies command:
    * mpm query dependencies
* query pom command:
    * mpm query project/build

## Opinionated Frameworks
### minimal java library project
    mpm init 
based on canonical maven directory layout
### minimal REST project
    mpm init-sparkjava
based on SparkJava
### minimal WEB TEMPLATE project
    mpm init-sparkjava-web
based on SparkJava Handelbars
### minimal REST with diagnostic project
    mpm init-dropwizard
based on DropWizard
### minimal REST microservice project
    mpm init-springboot
based on SpringBoot

## Opnionated Architectures
### minimal garage architecture
1 - ddd directories & interfaces


    mkdir proj1
    cd proj1
    mpm init-garage

2 - domain, usecase, acceptance test templates


    mvn test compile

3 - shell adapter template

4 - filtered resources properties config


    java -cp target/classes it.nipe.garage.adapter.shell.ShellApplication

5 - site with changelog, license, project info


    mvn site
    firefox target/site/index.html&

6 - intelliJ and gitignore ready.
 
    git init
    idea .
    
## To Do

### v.1.0.0
* fix init-*
* add ask
* add template
* add garage-arki

### v.1.1.0
* complete git interface

### Wish backlog
* add ask init required args. 
* add generic POM element update.
* add local element template library.
* add onion-architecture
* add clean-architecture
* add ddd-architecture
* add layered(inverse)-architecture
* add patterns
* add koans

## Tecnical Issues

* REFACT: Extract internal commands (quit, help, layout).
