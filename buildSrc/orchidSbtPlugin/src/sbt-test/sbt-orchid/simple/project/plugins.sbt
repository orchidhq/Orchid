resolvers += Resolver.mavenLocal // hosts locally-built artifacts
resolvers += Resolver.jcenterRepo // hosts Orchid and its components

val OrchidVersion = (sys.props.get("orchid.version") orElse sys.env.get("GRADLE_PROJECT_RELEASE_NAME")).getOrElse( sys.error("Missing Orchid Version") )

sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("io.github.javaeden.orchid" % "sbt-orchid" % x)
  case _ => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}

/*
 *  Add desired Orchid components to the build
 */
 
def orchidComponent( name : String ) = "io.github.javaeden.orchid" % name % OrchidVersion

/*
 *  The plugin includes OrchidCore already as a dependency,
 *  but explicitly specifying it endures version consistency.
 */
 
libraryDependencies += orchidComponent( "OrchidCore" )
libraryDependencies += orchidComponent( "OrchidPages" )
libraryDependencies += orchidComponent( "OrchidBsDoc" )

// libraryDependencies += "org.sharegov" % "mjson" % "1.4.1"

