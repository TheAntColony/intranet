package DAL.Repository

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject._
import DAL.Models._
import DAL.Traits.ILeaveRepository

class LeaveRepository @Inject()()
    extends BaseRepository() with ILeaveRepository {

  def create(vacation: Leave): Future[Option[Long]] = {
    val vacationIdQuery = (leaves returning leaves.map(_.id)) += vacation
    runCommand(vacationIdQuery).map(vacationId => {
      Some(vacationId)
    }).recover {
      case _: Exception => None
    }
  }

  def update(leave: Leave) : Future[Option[Leave]] = {
    val mapUpdateAction = leaves.filter(_.id === leave.id)
      .map(dbLeave => (dbLeave.reason, dbLeave.status, dbLeave.startDate, dbLeave.endDate, dbLeave.categoryId, dbLeave.userId))
      .update( (leave.reason, leave.status, leave.startDate, leave.endDate, leave.categoryId, leave.userId))

    runCommand(mapUpdateAction)
      .map(updateCount => {
        if (updateCount <= 0) {
          None
        } else {
          Some(leave)
        }
      })
      .recover {
        case _ : Exception => None
      }
  }

  def delete(id: Long): Future[Int] = {
    runCommand(leaves.filter(_.id === id).delete)
  }

  def getById(id: Long): Future[Option[Leave]] = {
    runCommand(leaves.filter(_.id === id).result).map(_.headOption)
  }

  def getTotalByStatus(oStatus: Option[String]): Future[Int] = {
    val command = oStatus.fold(leaves.length.result)(status => {
      leaves.filter(_.status === status).length.result
    })
    runCommand(command)
  }

  def getByUserId(userId: Long): Future[Seq[(Leave, LeaveCategory)]] = {
    val innerJoin = (for {
      (c, s) <- leaves join leaveCategories on (_.categoryId === _.id)
    } yield (c, s))
      .filter(_._1.userId === userId)
      .result

    runCommand(innerJoin)
  }

  def get: Future[Seq[(Leave, LeaveCategory, User)]] = {
    val innerJoin = (for {
      ((leave, category), user) <-
        leaves join leaveCategories on (_.categoryId === _.id) join users on (_._1.userId === _.id)
    } yield (leave, category, user)).result

    runCommand(innerJoin)
  }
}
