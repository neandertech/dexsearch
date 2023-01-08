import org.scalajs.linker.interface.StandardConfig
enablePlugins(ScalaJSPlugin)
enablePlugins(NpmPackagePlugin)

name := "dexsearch"
version := "0.1.5"

scalaVersion := "3.2.1" // or any other Scala version >= 2.11.12

libraryDependencies ++= Seq(
  "com.monovore" %%% "decline-effect" % "2.4.1"
)

// This is an application with a main method
scalaJSUseMainModuleInitializer := true
Compile / scalaJSLinkerConfig := {
  val c = scalaJSLinkerConfig.value
  c.withModuleKind(ModuleKind.ESModule).withJSHeader("#!/usr/bin/env node\n")
}
licenses := Seq(License.Apache2)

npmPackageAuthor := "Neandertech"
npmPackageDescription := "CLI that uses the Scaladex API to quickly search libraries and copy them to the clipboard"
npmPackageName := "dexsearch"
npmPackageNpmrcScope := Some("neandertech")
npmPackageBinaryEnable := true
npmPackageDependencies ++= {
  Seq(
    "node-fetch" -> "^2.6.1",
    "prompts" -> "^2.4.2",
    "clipboardy" -> "^3.0.0"
  )
}
