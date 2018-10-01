package de.kappmeier.asnarc.board

import de.kappmeier.asnarc.AsnarcSpec
import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.elements.Element

class AsnarcBoardTest extends AsnarcSpec {

  class FreeElement(val p: Point) extends Element {
    override def connects: Set[Direction] = Set.empty[Direction]
  }

  "board size" should "be computed correctly" in {
    val map: Map[Point, Element] = Map(
      Point(1, 1) -> new FreeElement(Point(1, 1)),
      Point(1, 2) -> new FreeElement(Point(1, 2)),
      Point(2, 1) -> new FreeElement(Point(2, 1)),
      Point(2, 2) -> new FreeElement(Point(2, 2)),
      Point(3, 1) -> new FreeElement(Point(3, 1)),
      Point(3, 2) -> new FreeElement(Point(3, 2))
    )
    val fixture: AsnarcBoard = new AsnarcBoard(map)

    fixture.rows should be(2)
    fixture.cols should be(3)
  }
}
