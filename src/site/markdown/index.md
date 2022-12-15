# KPRIME CLI - Command Line Interface Project

The purpose of KP CLI is to complement KPRIME SERVER tool with an interactive shell command.
 
## Usage

Define an environment variable KPRIME_HOME

define env.propeerties

### Inline command

It could be used as single command line:

    kp <args>

### Interactive 

Or it could be started in interactive shell mode: 

    kp

To list all available commands, press enter, giving an empty command.

To end just type:
    
    quit

To run a kprime command

    put <kprime-command>

such as:

    put help
    put help topic
    put help cmd
    put labels

## To Do

### v.1.0.0

* print cli info such as version.
* set server address.
* set user credentials.
* set context.
* print server info such as version.
* parse json server response to clean output response.
* get commands for autocomplete from server.

## Tecnical Issues

* .
