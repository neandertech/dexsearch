package dex

import cats.data.ValidatedNel
import cats.syntax.all._
import com.monovore.decline.Opts

enum BuildTool:
  case SBT
  case Mill
  case Bleep
  case ScalaCLI
  case Ammonite

object BuildTool:

  val metavar: String = s"[${BuildTool.values.mkString(", ")}]"

  def fromString(str: String): ValidatedNel[String, BuildTool] =
    str.toLowerCase() match
      case "sbt"              => BuildTool.SBT.valid
      case "mill"             => BuildTool.Mill.valid
      case "bleep"            => BuildTool.Bleep.valid
      case "scalacli" | "cli" => BuildTool.ScalaCLI.valid
      case "amm" | "ammonite" => BuildTool.ScalaCLI.valid
      case other =>
        s"Unkown build tool, expected one of [${BuildTool.values.mkString(", ")}]".invalidNel

  val opt: Opts[Option[BuildTool]] = Opts
    .option[String](
      long = "build-tool",
      short = "b",
      help = "Build tool that receives the dependency",
      metavar = metavar
    )
    .mapValidated[BuildTool](fromString)
    .orNone

end BuildTool
