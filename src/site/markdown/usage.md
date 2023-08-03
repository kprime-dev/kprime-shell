# Usage

Define one environment variable: 

    KPRIME_HOME

## First run

Run 

        $ kp

If it is the first time you run KP,
it will run the startup sequence asking the required runtime parameters

* server-name : the free to choose label you give to KP server address.
* server-address : the URL to KP server address.
* user-name : the user used as KP credentials.
* user-pass : the user password used as KP credentials.

 for example:

    server-name>dev
    server-address>http://localhost:7000
    user-name>nicola
    user-pass>pass

Information are written in file

    KPRIME_HOME/cli.properties

## Properties

Display current properties:

    >properties

As output, you get current properties, for example:

    [server-name]=[dev]
    [dev]=[http://localhost:7000]
    [user-name]=[nicola]
    [user-pass]=[pass]

You can remove one property with the command:

    >proprety-rem <property.name>

for example

    >proprety-rem context

You can set a new or override an existent property with the command:

    >property-set <property.name>=<property.value>

for example

    >proprety-set context=employee

## Contexts

First you have to define the context in which you want to operate.

If you want to create a new context:

    >add-context <context.name>

If you want to list current available contexts:

    >contexts

If you want to use the existing <context.name> 

    >property-set context=<context.name>

Now on CLI will remember the context for each command.
You can override the context for just one command with -context optional paramenter.

Example:

    >-context=employee goals

## Inline command

KP-CLI could be used as single command line:

    $ kp -context=<context.name> <kprime.command>

Example:

    $ kp -context=employee goals

## Interactive

KP-CLI could be started in interactive shell mode:

    $ kp

To list all available commands, press enter, giving an empty command or type command:

    >help

To end just type command:

    >quit

To run a kprime command just enter the command:

    ><kprime.command>

such as, to have a brief introduction:

    >help

to have the topic around which commands are catalogated:

    >help topic

to have the list of available serve command with brief description.

    >help cmd


Pressing TAB you could see the list of available command from the KP server you have choosed.

For each command is available a brief description:

    >goal ?
    
    Get one goal.

and a usage with required,optional parameters

    >goal ??

    goal : Get one goal.
    GOAL_ID : Goal ID default:null required:true
    