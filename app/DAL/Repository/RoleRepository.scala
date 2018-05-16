package DAL.Repository

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject._
import DAL.Models._
import DAL.Traits.IRoleRepository

class RoleRepository @Inject()() extends BaseRepository() with IRoleRepository {
  def create(role: Role): Future[Option[Long]] = {
    runCommand((roles returning roles.map(_.id)) += role)
      .map(roleId => {
        Some(roleId)
      }).recover {
        case _: Exception => None
      }
  }

  def delete(id: Long): Future[Int] = {
    runCommand(roles.filter(_.id === id).delete)
  }

  def getById(id: Long): Future[Option[Role]] = {
    runCommand(roles.filter(_.id === id).result).map(_.headOption)
  }

  def getByName(name: String): Future[Option[Role]] = {
    runCommand(roles.filter(_.name === name).result).map(_.headOption)
  }


  def get: Future[Seq[Role]] = {
    runCommand(roles.result)
  }
}
