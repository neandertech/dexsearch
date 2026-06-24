package dex

import cats.effect.IO

trait Scaladex:
  protected val target = "JVM"
  protected val scalaVersion = "2.13"

  def search(name: String): IO[Array[Project]]
  def project(org: String, repo: String): IO[ProjectDetails]

object Scaladex extends ScaladexPlatform
  

case class Project(
    organization: String,
    repository: String,
    artifacts: Array[String]
)

case class ProjectDetails(
    groupId: String,
    version: String,
    artifacts: Array[String],
    versions: Array[String]
)
