package src.portal;

import java.awt.*;

public class YellowPortals extends AbstractPortals {
  public YellowPortals() {
    super();
  }

  @Override
  protected String getKey() {
    return "Yellow";
  }

  @Override
  protected Color getPaintColor() {
    return Color.yellow;
  }
}
