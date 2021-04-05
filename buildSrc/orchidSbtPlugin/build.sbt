val OrchidVersion = (sys.props.get("orchid.version") orElse sys.env.get("GRADLE_PROJECT_RELEASE_NAME")).getOrElse( failNoVersion )

val isTestBuild =   {
  sys.props.get("orchid.testbuild").map( _.toLowerCase ) match {
    case Some( "true"  ) => true
    case Some( "false" ) => false
    case Some( huh     ) => sys.error( s"Bad value for 'orchid.testbuild': ${huh}. Should be 'true' or 'false' (or just omitted)." )
    case None            => false
  }
}

val BuildVersion = {
  if (isTestBuild) OrchidVersion + "-SNAPSHOT" else OrchidVersion
}

val PomExtraFragment = {
    <url>https://orchid.run/</url>
    <scm>
      <url>git@github.com:orchidhq/orchid.git</url>
      <connection>scm:git:git@github.com:orchidhq/orchid.git</connection>
    </scm>
    <developers>
      <developer>
        <id>JavaEden</id>
        <name>Casey Brooks</name>
        <email>cjbrooks12@gmail.com</email>
      </developer>
    </developers>
}

ThisBuild / organization        := "io.github.orchid-revival.orchid"
ThisBuild / version             := BuildVersion
ThisBuild / resolvers           += Resolver.mavenLocal
ThisBuild / resolvers           += Resolver.jcenterRepo
ThisBuild / licenses            += ("LGPL-3.0", url("http://www.opensource.org/licenses/lgpl-3.0.html"))

lazy val root = (project in file(".")).enablePlugins(SbtPlugin).settings (
  name := "sbt-orchid",
  sbtPlugin := true,
  libraryDependencies ++= Seq(
    "io.github.javaeden.orchid" % "OrchidCore" % OrchidVersion,
    "org.sharegov" % "mjson" % "1.4.1" // override version 1.4.0 of transitive dependency mjson, which has a badly formed pom.xml file
  ),
  pomExtra := PomExtraFragment,
  scriptedLaunchOpts := {
    scriptedLaunchOpts.value ++ Seq("-Dplugin.version=" + version.value, "-Dorchid.version=" + OrchidVersion)
  }
)

def failNoVersion = {
  sys.error( s"In order to build, orchidSbtPlugin requires that the system property 'orchid.version' or the (equivalently interpreted) environment variable 'GRADLE_PROJECT_RELEASE_NAME' be set.")
}

