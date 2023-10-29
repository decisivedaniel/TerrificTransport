package transport.logic.resources

trait Producer {
  def ship(amount : Int) : Producer
}
