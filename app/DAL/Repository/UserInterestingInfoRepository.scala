package DAL.Repository

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject._
import DAL.Models.UserInterestingInfo
import DAL.Traits._

class UserInterestingInfoRepository @Inject()() extends BaseRepository() with IUserInterestRepository {
  def create(userInterest: UserInterestingInfo): Future[Option[Long]] = {
    val userInterestIdQuery = (usersInterestingInfo returning usersInterestingInfo.map(_.id)) += userInterest
    runCommand(userInterestIdQuery)
      .map(userId =>  Some(userId))
      .recover {
      case _: Exception => None
    }
  }

  def delete(id: Long): Future[Int] = {
    runCommand(usersInterestingInfo.filter(_.id === id).delete)
  }

  def update(user: UserInterestingInfo) : Future[Option[UserInterestingInfo]] = {
    val mapUpdateAction = usersInterestingInfo.filter(_.id === user.id)
      .map(dbUser => (dbUser.description, dbUser.name))
      .update( (user.description, user.name))

    runCommand(mapUpdateAction)
      .map(updateCount => {
        if (updateCount <= 0) {
          None
        } else {
          Some(user)
        }
      })
      .recover {
      case ex : Exception => None
    }
  }

  def getById(id: Long): Future[Option[UserInterestingInfo]] = {
    runCommand(usersInterestingInfo.filter(_.id === id).result).map(_.headOption)
  }

  def get: Future[Seq[UserInterestingInfo]] = {
    runCommand(usersInterestingInfo.result)
  }
}