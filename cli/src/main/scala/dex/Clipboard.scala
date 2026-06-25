package dex

import cats.effect.IO

trait Clipboard:
  def copyToClipboard(lines: Seq[String]): IO[Unit]

object Clipboard extends ClipboardPlatform
