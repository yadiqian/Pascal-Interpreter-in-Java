program fibonacci;
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
    writeln(i, ret);
  end;
end.