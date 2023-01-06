package dex

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("os", JSImport.Namespace)
@js.native
object os extends js.Object {
  def platform(): String = js.native
}
