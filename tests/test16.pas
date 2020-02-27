program test16;
var
  a, b, c, min: real;

procedure findMin(x, y, z: real; var m: real); 
(* Finds the minimum of the 3 values *)

begin
  if x < y then
      m:= x
   else
      m:= y;
   
   if z < m then
      m:= z;
end;

begin
  writeln('Enter three numbers:');
  readln(a, b, c);
  findMin(a, b, c, min); (* Procedure call *)
   
  writeln('Minimum: ', min);
end.