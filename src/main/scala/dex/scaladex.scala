package dex

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Thenable
import cats.effect.IO

object scaladex:

  val target = "JVM"
  val scalaVersion = "2.13"

  def search(name: String): IO[js.Array[Project]] =
    val n = name.trim()
    ioFetch
      .get(
        s"https://index.scala-lang.org/api/search?q=$n&target=$target&scalaVersion=$scalaVersion"
      )
      .flatMap(_.json)
      .map { json =>
        json.asInstanceOf[js.Array[Project]].filter { project =>
          project.repository.contains(n) || project.artifacts
            .exists(_.contains(n))
        }
      }

  def project(org: String, repo: String): IO[ProjectDetails] =
    ioFetch
      .get(
        s"https://index.scala-lang.org/api/project?organization=$org&repository=$repo"
      )
      .flatMap(_.json)
      .map { json =>
        json.asInstanceOf[ProjectDetails]
      }
  end project

end scaladex

@js.native
trait Project extends js.Object:
  def organization: String = js.native
  def repository: String = js.native
  def artifacts: js.Array[String] = js.native

@js.native
trait ProjectDetails extends js.Object:
  def artifacts: js.Array[String]
  def versions: js.Array[String]
  def groupId: String
  def version: String
