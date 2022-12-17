# To Do

### version 1.0.0

+ set server address.
+ set user credentials.
+ set context.
+ print server info such as version.

## Tecnical Issues

- get commands for autocomplete from server.
    - currentReader = readerWithOptions(currentReader,List.of("alfa","beta"));
    - is not functioning at runtime. 

### set server address

read properties
refact from Shell:

            cliHomeProperties.load(new FileReader(cli_home+"cli.properties"));



set with a command a property on file and in memory

    server.<name1>=<address1>
    server.<name2>=<address2>
    server.<name3>=<address3>
    server.name=<name>

use server.name to get server address 
use address to do http calls
parametrize KPrimeRepository.kprimeAddress:

    KPrimeRepository
    String kprimeAddress = "http://localhost:7000";