package dex

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.Promise
import scala.scalajs.js.Thenable
import cats.effect.IO
import scala.scalajs.js.JSConverters._
import cue4s.*, catseffect.PromptsIO

def promptLibName(prompts: PromptsIO): IO[LibName] =
  prompts
    .io(Prompt.Input("Which library?"))
    .map(_.toEither)
    .flatMap(IO.fromEither)
    .map(LibName(_))

def promptProject(prompts: PromptsIO, project: js.Array[Project]): IO[Project] =
  val mapping = project.toList
    .map: p =>
      val title = s"${p.repository} from ${p.organization}"
      val value = p

      title -> p
    .toMap

  if mapping.isEmpty then IO.raiseError(CompletionError.Error("no repos found"))
  else
    prompts
      .io(Prompt.SingleChoice("From which repo?", mapping.keySet.toList.sorted))
      .map(_.toEither)
      .flatMap(IO.fromEither)
      .map(mapping(_))
end promptProject

def promptModules(prompts: PromptsIO, project: Project): IO[js.Array[String]] =
  val choices = project.artifacts.toList

  prompts
    .io(Prompt.MultipleChoice.withNoneSelected("Which modules?", choices))
    .map(_.toEither)
    .flatMap(IO.fromEither)
    .map(_.toJSArray)
end promptModules

def promptVersion(prompts: PromptsIO, project: ProjectDetails): IO[String] =
  prompts
    .io(Prompt.SingleChoice("Which version?", project.versions.toList))
    .map(_.toEither)
    .flatMap(IO.fromEither)
end promptVersion

def promptBuildTool(prompts: PromptsIO): IO[BuildTool] =
  prompts
    .io(
      Prompt.SingleChoice(
        "Which build tool?",
        BuildTool.values.map(_.toString).toList
      )
    )
    .map(_.toEither)
    .flatMap(IO.fromEither)
    .map(BuildTool.valueOf)
end promptBuildTool
