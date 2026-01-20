import java.nio.file.{Files, Path, Paths}
import scala.jdk.CollectionConverters.*

object CapabilitiesVersion {
  private val FilePath: Path = Paths.get("resources/protagonists.csv")

  trait ReadLines {
    def readLines(path: Path): List[String]
  }
  private def readLines(path: Path): ReadLines ?-> List[String] = { r ?=> r.readLines(path) }
  
  trait ReadNumber {
    def readNumber(): Option[Int]
  }
  private val readNumber: ReadNumber ?-> Option[Int] = { r ?=> r.readNumber() }
  
  trait Print {
    def printLine(str: String): Unit
  }
  private def printLine(str: String): Print ?-> Unit = { p ?=> p.printLine(str) }
  
  trait WriteLines {
    def writeLines(path: Path, lines: List[String]): Unit
  }
  private def writeLines(path: Path, lines: List[String]): WriteLines ?-> Unit = { w ?=> w.writeLines(path, lines) }
  
  private val askForUpdate: (Print, ReadNumber) ?-> Int = {
    printLine("By how much should I update the age? ")
    readNumber.getOrElse {printLine("Invalid input"); 0}
  }

  private def updateAge(p: Protagonist, n: Int): Print ?-> Protagonist = {
    val newAge = p.age + n
    printLine(s"The age of ${p.firstName} ${p.lastName} changes from ${p.age} to $newAge\n")
    p.copy(age = newAge)
  }

  type RunType = (ReadLines, ReadNumber, Print, WriteLines) ?-> Unit

  val run: RunType = {
    val lines        = readLines(FilePath)
    val protagonists = lines.map(Protagonist.fromLine)
    val n            = askForUpdate
    val updated      = protagonists.map(updateAge(_, n))
    val newLines     = updated.map(_.toLine)
    writeLines(FilePath, newLines)
  }

  private object System {
    given ReadLines  = (path: Path)                      => Files.readAllLines(path).asScala.toList
    given ReadNumber = ()                                => scala.io.StdIn.readLine().toIntOption
    given Print      = (str: String)                     => printf(str)
    given WriteLines      = (path: Path, lines: List[String]) => Files.writeString(path, lines.mkString("\n"))

    inline def apply(run: RunType): Unit = run
  }

  @main def main(): Unit = System(run)
}
