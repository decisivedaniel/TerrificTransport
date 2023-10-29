package transport.logic.resources

trait Consumer {
  def delivery(amount : Int) : Consumer
}
