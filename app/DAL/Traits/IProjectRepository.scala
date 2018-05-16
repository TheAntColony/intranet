package DAL.Traits

import DAL.Models.{Project, Skill}

import scala.concurrent.Future

trait IProjectRepository {
  def create(project: Project): Future[Option[Long]]

  def delete(id: Long): Future[Int]

  def update(project: Project) : Future[Option[Project]]

  def getById(id: Long): Future[Option[Project]]

  def get: Future[Seq[(Project, Seq[Skill])]]
}
