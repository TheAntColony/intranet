package DAL.Traits

import DAL.Models.{User, UserDetail}

import scala.concurrent.Future

trait IUserDetailRepository {
  def create(user: UserDetail): Future[Option[Long]]

  def delete(id: Long): Future[Int]

  def update(user: UserDetail) : Future[Option[UserDetail]]

  def getById(id: Long): Future[Option[UserDetail]]

  def getByUserId(userId: Long): Future[Option[UserDetail]]

  def get: Future[Seq[UserDetail]]

  def getWithUser: Future[Seq[(User, UserDetail)]]

  def getWithOffsetAndLimit(offset: Long, limit: Long): Future[Seq[(User, UserDetail)]]
}
