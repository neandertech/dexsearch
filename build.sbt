import org.scalajs.linker.interface.StandardConfig
enablePlugins(ScalaJSPlugin)
enablePlugins(NpmPackagePlugin)

name := "dex"
scalaVersion := "3.2.1" // or any other Scala version >= 2.11.12

libraryDependencies ++= Seq(
  "com.monovore" %%% "decline-effect" % "2.4.1"
)

// This is an application with a main method
scalaJSUseMainModuleInitializer := true
scalaJSLinkerConfig ~= (_
  /* disabled because it somehow triggers many warnings */
  .withSourceMap(false)
  .withModuleKind(ModuleKind.CommonJSModule))

licenses := Seq(License.Apache2)

npmPackageAuthor := "Neandertech"
npmPackageDescription := "CLI that uses the Scaladex API to quickly search libraries and copy them to the clipboard"
npmPackageName := "dexsearch"
npmPackageNpmrcScope := Some("neandertech")
npmPackageBinaryEnable := true
