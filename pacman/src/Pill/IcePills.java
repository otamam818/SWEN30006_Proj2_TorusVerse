package src.Pill;

import java.awt.*;

public class IcePills extends AbstractPills {
  public IcePills() {
    super();
  }

  @Override
  protected String getKey() {
    return "Ice";
  }

  @Override
  protected Color getPaintColor() {
    return Color.blue;
  }
}
