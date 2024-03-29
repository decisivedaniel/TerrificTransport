package transport.logic

// you can alter this file!

sealed abstract class CellType

case class SnakeHead(direction: Direction) extends CellType

/** 0 is just before SnakeHead, 1.0 is tail */
case class SnakeBody(distanceToHead: Float = 0f) extends CellType

case class Empty() extends CellType

case class Cursor() extends CellType

case class Track() extends CellType

case class Route() extends CellType

case class DisplayCity(title: String) extends CellType