program test15;
var
   a, b, ret : real;

(*function definition *)
function max(num1, num2: real): real;
var
   (* local variable declaration *)
  result: real;
begin
  if (num1 > num2) then
    result := num1
  else
  begin
    result := num2;
  end;
  max := result;
end;

begin
  a := 100.0;
  b := 200.0;
  (* calling a function to get max value *)
  ret := max(400, 1020);
  writeln( 'Max value is:', ret );

  ret := max(a, b);   
  writeln( 'Max value is:', ret );
end.