package dex

import cats.effect.IO

import sttp.client4.*
import sttp.client4.upicklejson.default.*
import upickle.default.*
import sttp.client4.curl.CurlTryBackend

trait ScaladexPlatform extends Scaladex:

  given Reader[Project] = macroR
  given Reader[ProjectDetails] = macroR

  def search(name: String): IO[Array[Project]] =
    val n = name.trim
    IO.fromTry(
      basicRequest
        .get(
          uri"https://index.scala-lang.org/api/search?q=$n&target=$target&scalaVersion=$scalaVersion"
        )
        .response(asJsonOrFail[Array[Project]])
        .send(CurlTryBackend())
    ).map(_.body)

  def project(org: String, repo: String): IO[ProjectDetails] =
    IO.fromTry(
      basicRequest
        .get(
          uri"https://index.scala-lang.org/api/project?organization=$org&repository=$repo"
        )
        .response(asJsonOrFail[ProjectDetails])
        .send(CurlTryBackend())
    ).map(_.body)
