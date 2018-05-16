package DAL.Traits

import DAL.Models.LeaveCategory

import scala.concurrent.Future

trait ILeaveCategoryRepository {
  def create(leaveCategory: LeaveCategory): Future[Option[Long]]

  def delete(id: Long): Future[Int]

  def getById(id: Long): Future[Option[LeaveCategory]]

  def get: Future[Seq[LeaveCategory]]
}
