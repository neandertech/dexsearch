package dex

import com.monovore.decline.Argument
import cats.data.ValidatedNel
import cats.data.Validated.Valid
import com.monovore.decline.Opts

case class LibName(value: String)
object LibName:
  val argument: Opts[Option[LibName]] =
    Opts
      .argument[String]("name of the library to search")
      .map(LibName(_))
      .orNone
