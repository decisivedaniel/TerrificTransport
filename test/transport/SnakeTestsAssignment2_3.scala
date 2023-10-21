package transport

import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import transport.reverse.ReverseTests

@RunWith(classOf[JUnitRunner])
class SnakeTestsAssignment2_3 extends SnakeTestSuite(
  new ReverseTests
)
