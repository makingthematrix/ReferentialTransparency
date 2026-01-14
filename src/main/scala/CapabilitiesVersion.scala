import java.nio.file.{Files, Path, Paths}
import scala.jdk.CollectionConverters.*

object CapabilitiesVersion {
  trait ReadLines  { def readFile(path: Path): List[String] }
  trait ReadNumber { def readNumber(): Option[Int] }
  trait Print      { def printLine(str: String): Unit }
  trait Write      { def writeLines(path: Path, lines: List[String]): Unit }

  private def read(path: Path)                           : ReadLines ?-> List[String] = { r ?=> r.readFile(path) }
  private val readNumber                                 : ReadNumber ?-> Option[Int] = { r ?=> r.readNumber() }
  private def printLine(str: String)                     : Print ?-> Unit             = { p ?=> p.printLine(str) }
  private def writeLines(path: Path, lines: List[String]): Write ?-> Unit             = { w ?=> w.writeLines(path, lines) }

  private val FilePath: Path = Paths.get("resources/protagonists.csv")

  private val askForUpdate: (Print, ReadNumber) ?-> Int = {
    printLine("By how much should I update the age? ")
    readNumber.getOrElse { printLine("Invalid input"); 0 }
  }

  private def updateAge(p: Protagonist, n: Int): Print ?-> Protagonist = {
    val newAge = p.age + n
    printLine(s"The age of ${p.firstName} ${p.lastName} changes from ${p.age} to $newAge\n")
    p.copy(age = newAge)
  }

  type RunType = (ReadLines, ReadNumber, Print, Write) ?-> Unit

  val run: RunType = {
    val lines        = read(FilePath)
    val protagonists = lines.map(Protagonist.fromLine)
    val n            = askForUpdate
    val updated      = protagonists.map(updateAge(_, n))
    val newLines     = updated.map(_.toLine)
    writeLines(FilePath, newLines)
  }

  private object System {
    given ReadLines  = (path: Path) => Files.readAllLines(path).asScala.toList
    given ReadNumber = () => scala.io.StdIn.readLine().toIntOption
    given Print      = (str: String) => printf(str)
    given Write      = (path: Path, lines: List[String]) => Files.writeString(path, lines.mkString("\n"))

    inline def apply(run: RunType): Unit = run
  }

  @main def main(): Unit = System(run)
}
