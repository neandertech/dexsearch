import org.scalajs.linker.interface.StandardConfig
enablePlugins(ScalaJSPlugin)
enablePlugins(NpmPackagePlugin)

// name := "dexsearch"
// version := "0.1.5"

lazy val Versions = new {
  val Scala = "3.9.0-RC1"
}

lazy val root = project.in(file(".")).aggregate(dexsearch.projectRefs *)

lazy val dexsearch = projectMatrix
  .in(file("cli"))
  .nativePlatform(
    Seq(Versions.Scala),
    Seq.empty,
    _.enablePlugins(ForgeNativeBinaryPlugin).settings(
      Seq(
        libraryDependencies += "com.softwaremill.sttp.client4" %%% "core" % "4.0.25",
        libraryDependencies += "com.softwaremill.sttp.client4" %%% "upickle" % "4.0.25"
      )
    )
  )
  .jsPlatform(
    Seq(Versions.Scala),
    Seq.empty,
    _.enablePlugins(ScalaJSPlugin, NpmPackagePlugin).settings(
      scalaJSUseMainModuleInitializer := true,
      Compile / scalaJSLinkerConfig := {
        val c = scalaJSLinkerConfig.value
        c.withModuleKind(ModuleKind.ESModule)
          .withJSHeader("#!/usr/bin/env node\n")
      },
      npmPackageAuthor := "Neandertech",
      npmPackageDescription := "CLI that uses the Scaladex API to quickly search libraries and copy them to the clipboard",
      npmPackageName := "dexsearch",
      npmPackageNpmrcScope := Some("neandertech"),
      npmPackageBinaryEnable := true,
      npmPackageDependencies ++= {
        Seq(
          "node-fetch" -> "3.3.2",
          "clipboardy" -> "5.3.1"
        )
      }
    )
  )
  .settings(
    libraryDependencies ++= Seq(
      "com.monovore" %%% "decline-effect" % "2.6.2",
      "tech.neander" %%% "cue4s-cats-effect" % "dev"
    )
  )

inThisBuild(List(licenses := Seq(License.Apache2)))
