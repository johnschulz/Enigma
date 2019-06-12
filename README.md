# Enigma

To use the scheme-interpreter

Download all files.

Open terminal/bash and change to the directory in which Enigma is downloaded.

Compile all files in the terminal:

    javac -g -Xlint:unchecked enigma/*.java
    
Run:
    
    java -ea enigma.Main [configuration_file] [input_file] [output(optional)]

Example: 

    java -ea enigma.Main testing/correct/default.conf testing/correct/trivial.inp
    
Or
    
    java -ea enigma.Main testing/correct/default.conf testing/correct/car1.inp

Enjoy!


