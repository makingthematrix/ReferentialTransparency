case class Protagonist(firstName: String, lastName: String, age: Int) {
  def toLine: String = s"$firstName,$lastName,$age"
}

object Protagonist {
  def fromLine(line: String): Protagonist = {
    val arr = line.split(",")
    Protagonist(arr(0), arr(1), arr(2).toInt)
  }
}

