val checkOrchidContains = inputKey[Unit]("Check that a string is contained within the contents of an orchid-generated text file.")

checkOrchidContains := {
  try {
    import java.io.File
    import sbt.complete.DefaultParsers._

    val orchidDestDir = orchidDestination.value
    val (relFile, searchString ) = (Space ~> (StringBasic ~ (Space ~> StringBasic))).parsed
    val file = new File( orchidDestDir, relFile )
    val contents = scala.io.Source.fromFile( file ).getLines().mkString("\n")
    if ( contents.indexOf( searchString ) < 0 )
      throw new Exception( s"Expected String '${searchString}' in '${file}' not found." )
  }
  catch {
    case t : Throwable => {
      t.printStackTrace()
      throw t
    }
  }
}

lazy val root = (project in file(".")).settings(
  version := "0.0.1",
  orchidTheme := "BsDoc"
)



