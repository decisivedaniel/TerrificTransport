package transport.logic.resources

import transport.logic.Point
import scala.util.Random

abstract class KeyPoint(location : Point, storage : Map[String, Int]) {
  def isLocatedHere(p: Point) : Boolean = location == p
}

object KeyPoint {
  //Creator for building them
  private val cityNames: List[String] = List("Meppel", "Lelystad", "Leeuwarden", "Sneek", "Nijmegen", "Renkum", "Rheden",
    "Tiel", "Zutphen", "Groningen", "Kerkrade", "Maastricht", "Roermond", "Sittard", "Tegelen", "Venlo", "Oss",
    "Roosendaal", "s-Hertogenbosch", "Tilburg", "Amsterdam", "Den Helder", "Marken", "Velsen", "Volendam", "Zaanstad",
    "Kampen", "Zwolle", "Soest", "Utrecht", "Zeist", "Middelburg", "Vlissingen", "The Hague", "Scheveningen",
    "Katwijk", "Leiden", "Lisse", "Rijswijk", "Rotterdam", "Schiedam", "Vlaardingen", "Zoetermeer")
  private val maxStartPopulation = 2000

  def buildCity(location: Point) : KeyPoint = City(
    cityNames(Random.nextInt(cityNames.length)),
    location,
    Random.nextInt(maxStartPopulation),
    Map[String, Int]()
  )
}

abstract class Farm extends Producer {

}

case class Forest(storage: Int, rate : Float) extends Farm {
  override def ship(amount: Int): Producer = copy(storage = math.max(storage - amount, 0))
}

case class City(name : String, location: Point, population : Int, storage : Map[String, Int]) extends KeyPoint(location, storage) with Consumer {
  override def delivery(amount : Int): City = this
  override def toString(): String = s"$name: $population"

}