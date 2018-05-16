package DAL.Traits

import DAL.Models.Role

import scala.concurrent.Future

trait IRoleRepository {
  def create(role: Role): Future[Option[Long]]

  def delete(id: Long): Future[Int]

  def getById(id: Long): Future[Option[Role]]

  def getByName(name: String): Future[Option[Role]]

  def get: Future[Seq[Role]]
}
