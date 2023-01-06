package dex

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import cats.effect.IO

def copyToClipboard(lines: Seq[String]): IO[Unit] = IO {
  val platform = os.platform()
  if (platform == "darwin") {
    var proc = childProcess.spawn("pbcopy")
    proc.stdin.write(lines.mkString(System.lineSeparator()))
    proc.stdin.end();
  } else {
    sys.error(s"Platform $platform not supported, PRs are welcome")
  }
}
