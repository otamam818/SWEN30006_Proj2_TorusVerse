package src.pill;

import java.awt.*;

public class GoldPills extends AbstractPills {
  public GoldPills() {
    super();
  }

  @Override
  protected String getKey() {
    return "Gold";
  }

  @Override
  protected Color getPaintColor() {
    return Color.yellow;
  }
}
