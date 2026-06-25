package dex

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import cats.effect.IO

@JSImport("clipboardy", JSImport.Namespace)
@js.native
private object clipboardy extends js.Object:
  val default: ClipboardJS = js.native

@js.native
private trait ClipboardJS extends js.Any:
  def write(s: String): js.Promise[Unit] = js.native

trait ClipboardPlatform extends Clipboard:
  def copyToClipboard(lines: Seq[String]): IO[Unit] = IO.fromPromise {
    IO {
      clipboardy.default.write(lines.mkString(System.lineSeparator()))
    }
  }
