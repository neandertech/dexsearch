package dex

import scala.scalajs.js
import js.annotation.JSImport

/** Using this to discover the interface of objects, dynamically, to make sense
  * of the differences between
  */
@JSImport("util", JSImport.Namespace)
@js.native
object util extends js.Any:
  def inspect(any: js.Any): js.Any = js.native
