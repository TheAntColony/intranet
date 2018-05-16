package DAL.Traits

import DAL.Models.{Leave, LeaveCategory, User}

import scala.concurrent.Future

trait ILeaveRepository {
  def create(vacation: Leave): Future[Option[Long]]

  def update(leave: Leave) : Future[Option[Leave]]

  def delete(id: Long): Future[Int]

  def getById(id: Long): Future[Option[Leave]]

  def getByUserId(userId: Long): Future[Seq[(Leave, LeaveCategory)]]

  def getTotalByStatus(oStatus: Option[String]): Future[Int]

  def get: Future[Seq[(Leave, LeaveCategory, User)]]
}
