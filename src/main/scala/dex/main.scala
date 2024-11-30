package dex

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

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
import cue4s.catseffect.PromptsIO
import cue4s.CompletionError

object Main
    extends CommandIOApp("dex", "searches scaladex for Scala libraries"):

  val doneMessage = "Dependencies were copied to your clipboard, ready to paste"

  def main = (BuildTool.opt, LibName.argument).mapN {
    (maybeBuildTool, maybeLibName) =>
      PromptsIO()
        .use: prompts =>
          for
            libName <- maybeLibName
              .map(IO.pure(_))
              .getOrElse(promptLibName(prompts))
            projects <- scaladex.search(libName.value)
            project <- promptProject(prompts, projects)
            modules <- promptModules(prompts, project)
            details <- scaladex.project(
              project.organization,
              project.repository
            )
            version <- promptVersion(prompts, details)
            buildTool <- maybeBuildTool
              .map(IO.pure(_))
              .getOrElse(promptBuildTool(prompts))
            content = format(buildTool, details.groupId, modules, version)
            _ <- copyToClipboard(content)
            _ <- IO.println(doneMessage)
          yield ExitCode(0)
        .recoverWith:
          case CompletionError.Interrupted =>
            IO.println("cancelled").as(ExitCode.Error)
          case CompletionError.Error(msg) => IO.println(msg).as(ExitCode.Error)
  }
  end main

  def format(
      buildTool: BuildTool,
      groupId: String,
      modules: js.Array[String],
      version: String
  ): List[String] = modules.toList.map { case module =>
    buildTool match
      case BuildTool.SBT   => s""""$groupId" %% "$module" % "$version""""
      case BuildTool.Mill  => s"""ivy"$groupId::$module:$version""""
      case BuildTool.Bleep => s"""$groupId::$module:$version"""
      case BuildTool.ScalaCLI =>
        s"""//> using lib "$groupId::$module:$version""""
      case BuildTool.Ammonite =>
        s"""import $$ivy.`$groupId::$module:$version`"""
  }

end Main
