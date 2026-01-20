import java.nio.file.{Files, Path, Paths}
import scala.jdk.CollectionConverters.*
import cats.effect.{IO, IOApp}
import cats.syntax.all.*

object CatsEffectVersion extends IOApp.Simple {
  override def run: IO[Unit] =
    for {
      lines        <- readLines(FilePath)
      protagonists =  lines.map(Protagonist.fromLine)
      n            <- askForUpdate
      updated      <- protagonists.traverse(updateAge(_, n))
      newLines     =  updated.map(_.toLine)
      _            <- writeLines(FilePath, newLines)
    } yield ()

  private val FilePath: Path = Paths.get("resources/protagonists.csv")

  private def readLines(path: Path): IO[List[String]] =
    IO { Files.readAllLines(path).asScala.toList }

  private val askForUpdate: IO[Int] =
    for {
      _      <- IO.print("By how much should I update the age? ")
      answer <- IO(scala.io.StdIn.readLine())
    } yield answer.toInt

  private def updateAge(p: Protagonist, n: Int): IO[Protagonist] =
    for {
      newAge  =  p.age + n
      _       <- IO.println(s"The age of ${p.firstName} ${p.lastName} changes from ${p.age} to $newAge")
      updated =  p.copy(age = newAge)
    } yield updated

  private def writeLines(path: Path, lines: List[String]): IO[Unit] =
    IO { Files.writeString(path, lines.mkString("\n")) }
}
