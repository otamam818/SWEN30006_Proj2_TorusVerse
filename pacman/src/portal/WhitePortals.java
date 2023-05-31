package src.portal;

import java.awt.*;

public class WhitePortals extends AbstractPortals {
  public WhitePortals() {
    super();
  }

  @Override
  protected String getKey() {
    return "White";
  }

  @Override
  protected Color getPaintColor() {
    return Color.white;
  }
}
