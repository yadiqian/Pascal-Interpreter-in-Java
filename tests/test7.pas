progRam test7;
(* Test arithmetic operations with variables. *)

var
  a, b, c, d, e: real;
  f: boolean;

BEGIN
  a := 5.55;
  b := -18;
  c := 77;
  d := -(a * b) + c - b;
  writeln (d);
  e:= -a + b * (d + a);
  writeln (e);
  writeln(d - c + 1);
  d:= d - c + (a * (b + (c - d)));
  writeln (d);
  e := -exp(d) + -sin(a) / cos(b);
  writeln (e);
  e := a;
  writeln (e);
  f := true;
eND.

(*
Expected output:

1.94900000000000E+002                                                               
-3.61365000000000E+003                                                               
1.18900000000000E+002                                                               
-6.36345000000000E+002                                                               
1.01351343820439E+000                                                               
5.55000000000000E+000

*)
