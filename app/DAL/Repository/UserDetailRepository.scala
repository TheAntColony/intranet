package DAL.Repository

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject._
import DAL.Models.{User, UserDetail}
import DAL.Traits._

class UserDetailRepository @Inject()() extends BaseRepository() with IUserDetailRepository {
  def create(userDetail: UserDetail): Future[Option[Long]] = {
    val userIdQuery = (usersDetails returning usersDetails.map(_.id)) += userDetail
    runCommand(userIdQuery)
      .map(userId =>
         Some(userId))
      .recover {
        case _: Exception => None
    }
  }

  def delete(id: Long): Future[Int] = {
    runCommand(usersDetails.filter(_.id === id).delete)
  }

  def update(user: UserDetail) : Future[Option[UserDetail]] = {
    val mapUpdateAction = usersDetails.filter(_.id === user.id)
      .map(dbUser => (dbUser.gender, dbUser.skin, dbUser.weight, dbUser.height, dbUser.hair,
        dbUser.description, dbUser.religion, dbUser.age, dbUser.country))
      .update( (user.gender, user.skin, user.weight, user.height, user.hair, user.description,
        user.religion, user.age, user.country))

    runCommand(mapUpdateAction)
      .map(updateCount => {
        if (updateCount <= 0) {
          None
        } else {
          Some(user)
        }
      })
      .recover {
        case _ : Exception => None
    }
  }

  def getWithOffsetAndLimit(offset: Long, limit: Long): Future[Seq[(User, UserDetail)]] = {
    val innerJoin = (for {
      (c, s) <- users join usersDetails on (_.id === _.userId)
    } yield (c, s)).drop(offset).take(limit).result
    runCommand(innerJoin)
  }

  def getWithUser: Future[Seq[(User, UserDetail)]] = {
    val innerJoin = (for {
      (c, s) <- users join usersDetails on (_.id === _.userId)
    } yield (c, s)).result
    runCommand(innerJoin)
  }

  def getById(id: Long): Future[Option[UserDetail]] = {
    runCommand(usersDetails.filter(_.id === id).result).map(_.headOption)
  }

  def getByUserId(userId: Long): Future[Option[UserDetail]] = {
    runCommand(usersDetails.filter(_.userId === userId).result).map(_.headOption)
  }

  def get: Future[Seq[UserDetail]] = {
    runCommand(usersDetails.result)
  }
}

