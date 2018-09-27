package de.kappmeier.asnarc.board

object Direction extends Enumeration {
  type Direction = Value
  val Left: Direction = new DirectionVal("Left", Point(-1, 0), 37) {
    override def opposite: DirectionVal = Right
  }
  val Right: DirectionVal = new DirectionVal("Right", Point(1, 0), 39) {
    override def opposite: DirectionVal = Left
  }
  val Up: DirectionVal = new DirectionVal("Up", Point(0, -1), 38) {
    override def opposite: DirectionVal = Down
  }
  val Down: DirectionVal = new DirectionVal("Down", Point(0, 1), 40) {
    override def opposite: DirectionVal = Up
  }

  /**
    * Initializes a direction with a point reflecting the vector of the direction and its key code.
    *
    * @param direction the vector of the direction
    * @param keyCode   the key code belonging to the direction
    */
  abstract case class DirectionVal(name: String, direction: Point, keyCode: Int) extends super.Val(name) {
    def opposite: DirectionVal
  }

  /**
    * Converts the enum type `Direction` to the internal `DirectionVal` type.
    *
    * @param value a `Direction` value
    * @return the enum item as `DirectionVal`
    */
  implicit def convert(value: Value): DirectionVal = value.asInstanceOf[DirectionVal]

  private val keyMap: Map[Int, Direction] = Direction.values.toList.map(d => d.keyCode -> d).toMap

  /**
    * Returns the direction belonging to a key code. If the key code does not belong to a direction, `None` is returned.
    *
    * @param keyCode the key code for a given direction
    * @return the `Direction`, possibly `None`
    */
  def byKeyCode(keyCode: Int): Option[Direction] = keyMap.get(keyCode)

  /**
    * Returns the opposite `Direction` for a given `Direction`
    *
    * @param d the direction
    * @return the opposite direction
    */
  implicit def opposite(d: DirectionVal): Direction = d.opposite
}
