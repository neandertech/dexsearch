package dex

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("child_process", JSImport.Namespace)
@js.native
object childProcess extends js.Object {
  def spawn(string: String): Proc = js.native
}

trait Proc extends js.Object {
  def stdin: Writable
}

trait Writable extends js.Object {
  def write(string: String): Unit
  def end(): Unit
}
