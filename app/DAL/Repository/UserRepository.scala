package DAL.Repository

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject._
import DAL.Models._
import DAL.Traits._

class UserRepository @Inject()() extends BaseRepository() with IUserRepository {
  def create(user: User): Future[Long] = {
    val userIdQuery = (users returning users.map(_.id)) += user
    runCommand(userIdQuery).map(userId => {
      userId
    }).recover {
        case ex: Exception => println(ex); 0L
      }
  }

  def delete(id: Long): Future[Int] = {
    runCommand(users.filter(_.id === id).delete)
  }

  def update(user: User) : Future[Option[User]] = {
    val mapUpdateAction = users.filter(_.id === user.id)
      .map(dbUser => (dbUser.firstName, dbUser.lastName, dbUser.isVerified, dbUser.isDisabled))
      .update( (user.firstName, user.lastName, user.isVerified, user.isDisabled))

    runCommand(mapUpdateAction)
      .map(updateCount => {
        if (updateCount <= 0) None
        else Some(user)
      })
      .recover {
        case _ : Exception => None
    }
  }

  def getById(id: Long): Future[Option[User]] = {
    runCommand(users.filter(_.id === id).result).map(_.headOption)
  }

  def getByEmail(email: String): Future[Option[User]] = {
    runCommand(users.filter(_.email === email).result).map(_.headOption)
  }

  def getWithOffsetAndLimit(offset: Long, limit: Long): Future[Seq[User]] = {
    runCommand(users.drop(offset).take(limit).result)
  }

  def get: Future[Seq[User]] = {
    runCommand(users.result)
  }

  def getTotal: Future[Int] = {
    runCommand(users.length.result)
  }
}

