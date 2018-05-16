package DAL.Repository

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject._
import DAL.Models._
import DAL.Traits.ILeaveCategoryRepository

class LeaveCategoryRepository @Inject()() extends BaseRepository() with ILeaveCategoryRepository {
  def create(leaveCategory: LeaveCategory): Future[Option[Long]] = {
    val skillIdQuery = (leaveCategories returning leaveCategories.map(_.id)) += leaveCategory
    runCommand(skillIdQuery).map(skillId => {
      Some(skillId)
    }).recover {
      case e: Exception => println(e.getLocalizedMessage);None
    }
  }

  def delete(id: Long): Future[Int] = {
    runCommand(leaveCategories.filter(_.id === id).delete)
  }

  def getById(id: Long): Future[Option[LeaveCategory]] = {
    runCommand(leaveCategories.filter(_.id === id).result).map(_.headOption)
  }

  /*def getByName(name: String): Future[Option[LeaveCategory]] = {
    runCommand(leaveCategories.filter(_.name === name).result).map(_.headOption)
  }*/

  def get: Future[Seq[LeaveCategory]] = {
    runCommand(leaveCategories.result)
  }
}
