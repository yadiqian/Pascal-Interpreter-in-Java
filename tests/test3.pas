program test3;
(* Test special expressions *)

begin
  writeln (sqrt(4 + 12 / 3));
  writeln(sqrt(150));
  writeln (sin(1/2));
  writeln (sin(100));
  writeln (cos(1 - (2 * 3 - 5)));
  writeln (cos(-100.6));
  writeln (ln(1 / 5 + 3));
  writeln (ln(71 - 4 * 2.5));
  writeln (exp(3));
  writeln (exp(4 * (5.5 - 1)));
end.

(*
Expected output:

2.8284271247461901E+0000                                            
1.2247448713915890E+0001                                            
4.7942553860420300E-0001                                            
-5.0636564110975879E-0001                                            
1.0000000000000000E+0000                                            
9.9761802474625297E-0001                                            
1.1631508098056809E+0000                                            
4.1108738641733112E+0000                                            
2.0085536923187668E+0001                                            
6.5659969137330511E+0007

*)