import java.nio.file.Path

class CapabilitiesVersionSuite extends munit.FunSuite {
  
  test("test if read, update, write give valid results") {
    import CapabilitiesVersion.*
    // tracking the program
    var asked = 0
    var written: Option[List[String]] = None
    var writeCalls = 0
    val UpdateNumber = 5
    val inputLines: List[String] = List(
      "Ada,Lovelace,36",
      "Alan,Turing,41",
      "Grace,Hopper,85"
    )
    
    object Test {
      given ReadLines  = (path: Path)                      => inputLines
      given ReadNumber = ()                                => { asked +=1; Some(UpdateNumber) }
      given Print      = (str: String)                     => ()
      given WriteLines = (path: Path, lines: List[String]) => { writeCalls += 1; written = Some(lines) }

      inline def apply(run: RunType): Unit = run
    }

    Test(run)

    // assertions
    assertEquals(asked, 1, "askForUpdate should be called exactly once")
    assertEquals(writeCalls, 1, "write should be called exactly once")

    val expected = List(
      "Ada,Lovelace,41",
      "Alan,Turing,46",
      "Grace,Hopper,90"
    )
    assertEquals(written, Some(expected))
  }
}
