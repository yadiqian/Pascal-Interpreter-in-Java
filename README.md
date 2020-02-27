# Pascal Interpreter

```
antlr4 -visitor Pascal.g4
```

```
javac Main.java 
```

```
java Main tests/test1.pas 
```

nested while loops
supports function and procedure overloading
supports recursion!!!

- break and continue in if else within loop

## Notes
For loop variable cannot take real type so real numbers are rounded to the nearst integer for loop counting.

Real numbers that have integer values are outputted as integers.  