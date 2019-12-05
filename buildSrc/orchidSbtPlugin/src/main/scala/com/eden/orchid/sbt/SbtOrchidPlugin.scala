package com.eden.orchid.sbt

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin
import sbt.Def.Initialize
import sbt.complete.DefaultParsers._

import java.io.File

import scala.collection._
import scala.util.control.NonFatal

// very much inspired by
//   https://github.com/orchidhq/orchid/blob/fb4db01d7e3dd0874e8d01c691ec9e58f0c79982/buildSrc/orchidMavenPlugin/src/main/java/com/eden/orchid/maven/OrchidGenerateMainMojo.java

object SbtOrchidPlugin extends AutoPlugin {

  final object autoImport {

    val OrchidCommands = immutable.SortedSet( "build", "deploy", "serve" )

    // general config
    val orchidBaseUrl        = settingKey[String] ("The base URL for generted site links")
    val orchidDestination    = settingKey[File]   ("The directory into which orchid sites are generated")
    val orchidDiagnose       = settingKey[Boolean]("Allows running in diagnose mode")
    val orchidDryDeploy      = settingKey[Boolean]("Allows running a dry deploy instead of a full deploy") // ???
    val orchidEnvironment    = settingKey[String] ("The environment used to run the orchid site.")         // ???
    val orchidPort           = settingKey[Int]    ("The port to run the dev server on.")
    val orchidSource         = settingKey[File]   ("The top-level source directory for orchid-related files")
    val orchidResources      = settingKey[File]   ("The 'resources' directory that used directly by this plugin to generate sites")
    val orchidTheme          = settingKey[String] ("The theme that will be imposed on the generated orchid site")
    val orchidVersion        = settingKey[String] ("The version of the orchid site")

    // tasks
    val orchidBuild  = taskKey[Unit]("Builds the orchid site")
    val orchidDeploy = taskKey[Unit]("Deploys the orchid site")
    val orchidRun    = inputKey[Unit](s"""Runs an orchid command (one of ${OrchidCommands.mkString(" | ")})""")
    val orchidServe  = taskKey[Unit]("Serves the orchid site from the dev server")

    // optional tokens
    val orchidAzureToken     = settingKey[String] ("Optional token for publication on Azure")
    val orchidBitbucketToken = settingKey[String] ("Optional token for publication on Bitbucket")
    val orchidGithubToken    = settingKey[String] ("Optional token for publication on Github")
    val orchidGitlabToken    = settingKey[String] ("Optional token for publication on Gitlab")
    val orchidNetlifyToken   = settingKey[String] ("Optional token for publication on Netlify")
  }

  import autoImport._

  // definitions
  lazy val orchidDefaults : Seq[sbt.Def.Setting[_]] = Seq(

    // settings

    orchidBaseUrl     := "",
    orchidDestination := (Compile / target).value / "orchid",
    orchidDiagnose    := false,
    orchidDryDeploy   := false,
    orchidEnvironment := "debug",
    orchidPort        := 8080,
    orchidResources   := orchidSource.value / "resources",
    orchidSource      := (Compile / sourceDirectory).value / "orchid",
    orchidTheme       := "Default",
    orchidVersion     := version.value,

    // tasks

    orchidBuild  := { orchidExecuteTask("build").value },
    orchidDeploy := { orchidExecuteTask("deploy").value },
    orchidRun    := { orchidRunTask.evaluated },
    orchidServe  := { orchidExecuteTask("serve").value },
  )

  private def orchidRunTask = {
    val parser = (token(Space) ~> token(ID).examples(OrchidCommands)).?

    Def.inputTaskDyn {
      val mbCommand = parser.parsed
      val command = (mbCommand orElse sys.props.get( "orchid.runTask" )) getOrElse {
        sys.error( "No command supplied for orchidRun, neither on the command line nor as system property '-Dorchid.runTask'." )
      }
      orchidExecuteTask( command )
    }
  }

  private def orchidExecuteTask( command : String ) = Def.task {
    val log = streams.value.log

    val srcDir = orchidResources.value

    if (! srcDir.exists()) {
      log.warn( s"Orchid source directory '${srcDir}' does not exist. Your Orchid source documents should be there!" )
    }

    val baseUrl     = orchidBaseUrl.value
    val src         = srcDir.getAbsolutePath
    val dest        = orchidDestination.value.getAbsolutePath
    val theme       = orchidTheme.value
    val version     = orchidVersion.value
    val environment = orchidEnvironment.value
    val dryDeploy   = orchidDryDeploy.value.toString
    val port        = orchidPort.value.toString
    val diagnose    = orchidDiagnose.value.toString

    val azureToken     = orchidAzureToken.?.value
    val bitbucketToken = orchidBitbucketToken.?.value
    val githubToken    = orchidGithubToken.?.value
    val gitlabToken    = orchidGitlabToken.?.value
    val netlifyToken   = orchidNetlifyToken.?.value

    val argMap = mutable.Map[String,String] (
      "baseUrl"     -> baseUrl,
      "src"         -> src,
      "dest"        -> dest,
      "task"        -> command,
      "theme"       -> theme,
      "version"     -> version,
      "environment" -> environment,
      "dryDeploy"   -> dryDeploy,
      "port"        -> port,
      "diagnose"    -> diagnose
    )

    def addToken( name : String, mbToken : Option[String] ) : Unit = mbToken.foreach( token => argMap += Tuple2( name, token ) )

    addToken( "azureToken"    , azureToken )
    addToken( "bitbucketToken", bitbucketToken )
    addToken( "githubToken"   , githubToken )
    addToken( "gitlabToken"   , gitlabToken )
    addToken( "netlifyToken"  , netlifyToken )

    val argArray = argMap.foldLeft( Nil : List[String] )( ( accum, next ) => next._2 :: s"--${next._1}" :: accum ).reverse.toArray

    def doIt = {
      val old_sm = System.getSecurityManager()
      try {
        com.eden.orchid.Orchid.internalMain( argArray )
      }
      finally {
        if (! (System.getSecurityManager() eq old_sm) ) {
          try {
            System.setSecurityManager( old_sm )
          }
          catch {
            case NonFatal(t) => {
              log.warn( s"Cannot restore original SecurityManager. Orchid's resetting of a SecurityManager may cause problems" )
              log.trace( sbt.util.InterfaceUtil.toSupplier(t) )
            }
          }
        }
      }
    }

    // System.out.synchronized( doIt )
    doIt
  }

  // plug-in setup

  // very important to ensure the ordering of definitions,
  // so that JvmPlugin's compile actually gets overridden
  override def requires = JvmPlugin

  override def trigger = allRequirements

  override val projectSettings = orchidDefaults
}
