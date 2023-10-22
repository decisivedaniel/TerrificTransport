package transport.logic

case class Point(x : Int, y : Int) {
  def +(rhs : Point) : Point = copy(x + rhs.x, y + rhs.y)
}
