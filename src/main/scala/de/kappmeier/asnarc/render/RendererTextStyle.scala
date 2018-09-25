package de.kappmeier.asnarc.render

/**
  * Structure containing the font style for text rendered.
  *
  * @param font  the font style as html, i.e. "sans-serif"
  * @param size  the size in pixels
  * @param color the font color, i.e. #ff0000 or "darkgreen"
  */
case class RendererTextStyle(font: String, size: Int, color: String)
