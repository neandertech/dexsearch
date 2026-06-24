package dex

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import cats.effect.IO

@JSImport("node-fetch", JSImport.Default)
@js.native
object fetch extends js.Function1[String, js.Thenable[FetchResponse]]:
  override def apply(arg1: String): js.Thenable[FetchResponse] = js.native

trait FetchResponse extends js.Object:
  def status: Int
  def json(): js.Thenable[js.Any]
  def text(): js.Thenable[String]

object ioFetch:
  def get(url: String): IO[IOFetchResponse] =
    IO.fromThenable(IO(fetch(url))).map(IOFetchResponse(_))

case class IOFetchResponse(underlying: FetchResponse):
  export underlying.status

  def json: IO[js.Any] = IO.fromThenable(IO(underlying.json()))
  def text: IO[String] = IO.fromThenable(IO(underlying.text()))
