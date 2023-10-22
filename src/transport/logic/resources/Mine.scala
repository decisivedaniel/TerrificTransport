package transport.logic.resources

abstract class Farm extends Producer {

}

case class Forest(storage: Int, rate : Float) extends Farm {
  override def ship(amount: Int): Producer = copy(storage = math.max(storage - amount, 0))
}

case class City(name : String, population : Int, storage : Int) extends Consumer {
  override def delivery(amount : Int): City = copy(storage = amount + storage)

}

