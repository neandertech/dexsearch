package dex

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

// @main
def main() = println(util.inspect(prompts.default))

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.util.Success
import scala.util.Failure
import com.monovore.decline.CommandApp
import com.monovore.decline.Opts
import cats.syntax.all._
import com.monovore.decline.effect.CommandIOApp
import cats.effect.IO
import cats.effect.ExitCode

object Main
    extends CommandIOApp("dex", "searches scaladex for Scala libraries") {

  def main = {
    (BuildTool.opt, LibName.argument).mapN { (maybeBuildTool, maybeLibName) =>
      for {
        libName <- maybeLibName.map(IO.pure(_)).getOrElse(promptLibName)
        projects <- scaladex.search(libName.value)
        project <- promptProject(projects)
        modules <- promptModules(project)
        details <- scaladex.project(project.organization, project.repository)
        version <- promptVersion(details)
        buildTool <- maybeBuildTool.map(IO.pure(_)).getOrElse(promptBuildTool)
        content = format(buildTool, details.groupId, modules, version)
        _ <- copyToClipboard(content)
        _ <- IO.println(
          "Dependencies were copied to your clipboard, ready to paste"
        )
      } yield ExitCode(0)
    }
  }

  def format(
      buildTool: BuildTool,
      groupId: String,
      modules: js.Array[String],
      version: String
  ): List[String] = modules.toList
    .map { case module =>
      buildTool match
        case BuildTool.SBT   => s""""$groupId" %% "$module" % "$version""""
        case BuildTool.Mill  => s"""ivy"$groupId::$module:$version""""
        case BuildTool.Bleep => s"""$groupId::$module:$version"""
        case BuildTool.ScalaCLI =>
          s"""//> using lib "$groupId::$module:$version""""
        case BuildTool.Ammonite =>
          s"""import $$ivy.`$groupId::$module:$version`"""
    }
}
