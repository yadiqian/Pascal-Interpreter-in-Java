import java.lang.Exception;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      throw new Exception("Please add 1 argument.");
    }

    System.out.println("parsing: " + args[0]);
    
    PascalLexer lexer = new PascalLexer(CharStreams.fromFileName(args[0]));
    PascalParser parser = new PascalParser(new CommonTokenStream(lexer));
    ParseTree tree = parser.program();
    MyVisitor visitor = new MyVisitor();
    visitor.visit(tree);
  }
}