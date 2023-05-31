package src.portal;

import java.awt.*;

public class DarkGrayPortals extends AbstractPortals {
  public DarkGrayPortals() {
    super();
  }

  @Override
  protected String getKey() {
    return "DarkGray";
  }

  @Override
  protected Color getPaintColor() {
    return Color.darkGray;
  }
}
