program test13;
var
  a: real;
  b: boolean;

begin
  a := 10;
  while a < 20 do
  
  begin
    writeln('value of a: ', a);
    a := a + 1;
  end;

  while (a > 8) do
    a := a - 2;
  writeln('value of a: ', a);

  b := true;
  while (b) do
  begin
    if (a > 0) then
      a := a - 1
    else
      b := false;
  end;

  writeln('value of a: ', a);
  writeln('value of b: ', b);

end.