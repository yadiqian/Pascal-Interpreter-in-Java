program fibonacci;
(* Test recursive function call *)
var
  ret, i : real;

function fibonacci(n:real): real;
begin
  if n > 1 then
  fibonacci := fibonacci(n - 1) + fibonacci(n - 2)
  else
    fibonacci := n;
end;

begin
  for i := 0 to 20 do 
  begin
    ret := fibonacci(i);
    writeln(i,' ', ret);
  end;
end.

(* 
Expected output:

0 0
1 1
2 1
3 2
4 3
5 5
6 8
7 13
8 21
9 34
10 55
11 89
12 144
13 233
14 377
15 610
16 987
17 1597
18 2584
19 4181
20 6765

*)