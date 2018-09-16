package de.kappmeier.asnarc.board

/**
  * Simple integral point implementation. Used to identify the fields in
  *
  * @param x the horizontal position
  * @param y the vertical position
  */
case class Point(x: Int, y: Int) {
  def +(p: Point) = Point(x + p.x, y + p.y)

  def /(d: Int) = Point(x / d, y / d)

  def %(w: Int, h: Int) = Point((x + w) % w, (y + h) % h)
}
