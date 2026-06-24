package dex

import cats.effect.IO
import cats.syntax.all.*

import scalanative.meta.LinktimeInfo

trait ClipboardPlatform extends Clipboard:
  def copyToClipboard(lines: Seq[String]): IO[Unit] =
    provider.copyToClipboard(lines)

val provider =
  if LinktimeInfo.isMac then MacosProvider
  else if LinktimeInfo.isWindows then WindowsProvider
  else if LinktimeInfo.isLinux then LinuxProvider
  else Fallback

object Fallback extends Clipboard:
  override def copyToClipboard(lines: Seq[String]): IO[Unit] =
    val console = cats.effect.std.Console[IO]

    import console.*

    for
      _ <- errorln(
        "Your system does not have any known clipboard managing tools"
      )
      _ <- errorln("Here are the lines that would've been copied:")
      _ <- lines.traverse(errorln(_))
    yield ()

object MacosProvider extends Clipboard:
  override def copyToClipboard(lines: Seq[String]): IO[Unit] = IO(
    pipe(lines, Seq("pbcopy"))
  )

object LinuxProvider extends Clipboard:
  override def copyToClipboard(lines: Seq[String]): IO[Unit] =
    IO(pipe(lines, Seq("xsel", "--clipboard", "--input"))).recoverWith(_ =>
      IO(pipe(lines, Seq("wl-copy", "--type", "text/plain")))
    )

object WindowsProvider extends Clipboard:
  override def copyToClipboard(lines: Seq[String]): IO[Unit] =
    IO(pipe(lines, Seq("clip.exe")))

private def pipe(lines: Seq[String], process: Seq[String]) =
  import java.lang.process.*
  val p = ProcessBuilder.apply(process*)
  val s = p.start()
  val stream = s.getOutputStream()
  stream.write(lines.mkString(System.lineSeparator()).getBytes())

  stream.close()

  assert(
    s.waitFor() == 0,
    s"Process [${process.mkString(" ")}] exited with code ${s.exitValue()}"
  )
