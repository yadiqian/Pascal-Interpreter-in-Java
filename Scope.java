import java.util.HashMap;

class Scope extends HashMap<String, String[]> {

  final Scope parent;

  public Scope(Scope parent) {
      this.parent = parent;
  }

  public int inScope(String varName, int index) {
      if(super.containsKey(varName)) {
          return index;
      }
      return parent == null ? -1 : parent.inScope(varName, index - 1);
  }

}