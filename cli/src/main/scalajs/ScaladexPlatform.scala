package dex

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.Thenable
import cats.effect.IO

trait ScaladexPlatform extends Scaladex:


  def search(name: String): IO[Array[Project]] =
    val n = name.trim()
    ioFetch
      .get(
        s"https://index.scala-lang.org/api/search?q=$n&target=$target&scalaVersion=$scalaVersion"
      )
      .flatMap(_.json)
      .map { json =>
        json
          .asInstanceOf[js.Array[ProjectJS]]
          .filter { project =>
            project.repository.contains(n) || project.artifacts
              .exists(_.contains(n))

          }
          .map { json =>
            Project(
              organization = json.organization,
              repository = json.repository,
              artifacts = json.artifacts.toArray
            )
          }
          .toArray
      }

  def project(org: String, repo: String): IO[ProjectDetails] =
    ioFetch
      .get(
        s"https://index.scala-lang.org/api/project?organization=$org&repository=$repo"
      )
      .flatMap(_.json)
      .map { json =>
        json.asInstanceOf[ProjectDetailsJS]
      }
      .map { json =>
        ProjectDetails(
          groupId = json.groupId,
          version = json.version,
          artifacts = json.artifacts.toArray,
          versions = json.versions.toArray
        )
      }
  end project

@js.native
trait ProjectJS extends js.Object:
  def organization: String = js.native
  def repository: String = js.native
  def artifacts: js.Array[String] = js.native

@js.native
trait ProjectDetailsJS extends js.Object:
  def artifacts: js.Array[String]
  def versions: js.Array[String]
  def groupId: String
  def version: String
