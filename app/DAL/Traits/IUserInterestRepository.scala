package DAL.Traits

import DAL.Models.UserInterestingInfo

import scala.concurrent.Future

trait IUserInterestRepository {
  def create(user: UserInterestingInfo): Future[Option[Long]]

  def delete(id: Long): Future[Int]

  def update(user: UserInterestingInfo) : Future[Option[UserInterestingInfo]]

  def getById(id: Long): Future[Option[UserInterestingInfo]]

  def get: Future[Seq[UserInterestingInfo]]
}
