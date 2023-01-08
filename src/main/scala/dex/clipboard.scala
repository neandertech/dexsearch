package dex

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import cats.effect.IO

@JSImport("clipboardy", JSImport.Namespace)
@js.native
object clipboardy extends js.Object:
  val default: Clipboard = js.native

@js.native
trait Clipboard extends js.Any:
  def write(s: String): js.Promise[Unit] = js.native

def copyToClipboard(lines: Seq[String]): IO[Unit] = IO.fromPromise {
  IO {
    clipboardy.default.write(lines.mkString(System.lineSeparator()))
  }
}
