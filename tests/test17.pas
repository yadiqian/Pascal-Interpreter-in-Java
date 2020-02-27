program fibonacci;
var
  ret : real;

(*function definition *)
function fibonacci(n:real): real;
begin
  if n > 1 then
  fibonacci := fibonacci(n - 1) + fibonacci(n - 2)
  else
    fibonacci := n;
end;

begin
  ret := fibonacci(20);
  writeln(ret);
end.