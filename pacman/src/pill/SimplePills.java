package src.pill;

import java.awt.*;

public class SimplePills extends  AbstractPills {
  public SimplePills() {
    super();
  }

  @Override
  protected String getKey() {
    return "Pills";
  }

  protected Color getPaintColor() {
    return null;
  }
}