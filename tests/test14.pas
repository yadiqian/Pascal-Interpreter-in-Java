program test14;
var
  a: real;

begin
  for a := 10 to 20 do   
    begin
      writeln('value of a: ', a);
    end;

   writeln();

  for a := 10 * 5 downto 20 * 2 do  
    begin
      writeln('value of a: ', a / 2);
    end;
end.