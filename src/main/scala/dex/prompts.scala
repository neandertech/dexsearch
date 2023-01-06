package dex

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.Promise
import scala.scalajs.js.Thenable
import cats.effect.IO
import scala.scalajs.js.JSConverters._

@js.native
@JSImport("prompts", "prompt")
def prompts[T](x: js.Object): Promise[T] = js.native

def ioPrompt[T](x: js.Object): IO[T] =
  IO.fromPromise(IO(prompts(x)))

def promptLibName: IO[LibName] = {
  val libNameQuestion = new js.Object {
    val `type` = "text"
    val name = "libName"
    val message = "Which library ?"
  }

  trait LibNameAnswer extends js.Object:
    val libName: String

  ioPrompt[LibNameAnswer](libNameQuestion).map(answer =>
    LibName(answer.libName)
  )
}

def promptProject(project: js.Array[Project]): IO[Project] = {
  val projectQuestion = new js.Object {
    val `type` = "autocomplete"
    val name = "project"
    val message = "From which repo ?"
    val choices = project.map { p =>
      new js.Object {
        val title = s"${p.repository} from ${p.organization}"
        val value = p
      }
    }
  }

  trait ProjectAnswer extends js.Object:
    val project: Project

  ioPrompt[ProjectAnswer](projectQuestion).map(_.project)
}

def promptModules(project: Project): IO[js.Array[String]] = {
  val moduleQuestion = new js.Object {
    val `type` = "autocompleteMultiselect"
    val name = "modules"
    val message = "Which modules ?"
    val choices = project.artifacts.map { artifact =>
      new js.Object {
        val title = artifact
        val value = artifact
      }
    }
  }

  trait ProjectAnswer extends js.Object:
    val modules: js.Array[String]

  ioPrompt[ProjectAnswer](moduleQuestion).map(_.modules)
}

def promptVersion(project: ProjectDetails): IO[String] = {
  val versionQuestion = new js.Object {
    val `type` = "autocomplete"
    val name = "version"
    val message = "Which version ?"
    val choices = project.versions.map { version =>
      new js.Object {
        val title = version
        val value = version
      }
    }
  }

  trait VersionAnswer extends js.Object:
    val version: String

  ioPrompt[VersionAnswer](versionQuestion).map(_.version)
}

def promptBuildTool: IO[BuildTool] = {
  val versionQuestion = new js.Object {
    val `type` = "select"
    val name = "buildTool"
    val message = "Which build tool ?"
    val choices = BuildTool.values.map { bt =>
      new js.Object {
        val title = bt.toString()
        val value = bt
      }
    }.toJSArray
  }

  trait VersionAnswer extends js.Object:
    val buildTool: BuildTool

  ioPrompt[VersionAnswer](versionQuestion).map(_.buildTool)
}
