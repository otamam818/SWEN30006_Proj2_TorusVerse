package src.Pill;

import java.awt.*;
import java.util.Properties;

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
