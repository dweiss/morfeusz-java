morfeusz-java
=============

Java binding to Morfeusz Morphological analyser

Morfeusz is a morphological analyzer for the Polish language.
Copyright by Marcin Woliński and Zygmunt Saloni
http://www.ipipan.waw.pl/~wolinski/

Morfeusz-Java bridge Copyright (c) 2005-2008 by Dawid Weiss

***
 NOTE THAT THIS BINDING'S BINARY RELEASES DO NOT CONTAIN
            MORFEUSZ IN BINARY DISTRIBUTION

You need to download and install them separately.

version.txt has the Morfeusz version this package was
compiled against (it may, or may not work with newer
Morfeusz releases).

http://sgjp.pl/morfeusz/
***

Once you download Morfeusz's libraries and make them available
to your system, the following command should work and display
Morfeusz DLL/ JNI bridge's DLL versions:

```bash
java -jar MorfeuszJavaBridge.jar -version
```

The command-line demo application simply processes the input
tokens and displays their analysis. Example:

```bash
java -jar MorfeuszJavaBridge.jar -encoding Cp1250 input.txt output.txt  
```