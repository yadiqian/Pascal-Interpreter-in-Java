# Pascal Interpreter

Author: Yadi Qian

The following are implemented in this project:

* while-do and for-do loops
* break and continue keywords
* user-defined procedures and functions
* all parts implemented in project 1

## Commands to Compile and Run 

Generate and compile:
```
antlr4 -visitor Pascal.g4; javac Main.java 
```

Run tests:
```
java Main tests/test1.pas 
```
## Test Files
The tests folder contains 24 pascal test files. The first 12 are from project 1 to test features implemented in project 1. The last 12 tests are newly added to test functionalities implemented in this project. The expected output for each test file is in the comment section of each file.

## Extra Credit
Parameter passing in procedures/functions is implemented. Parameters that are passed by reference in procedure is also implemented (see ```test16.pas```). All parameter variables are correctly scoped. 

## Notes
For loop variable cannot take real type so real numbers are rounded to the nearst integer for loop counting.

Real numbers that have integer values are outputted as integers for an easier time of reading outputs. 

Here is a list of highligted functionalities of my interpreter:

* nested while and for loops
* function and procedure overloading
* recursion
* break and continue keywords are only allowed in loops, including in if-then-else statement within loops