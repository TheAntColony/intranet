package DAL.Repository

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject._
import DAL.Models._
import DAL.Traits._

class UserRoleRepository @Inject()() extends BaseRepository() with IUserRoleRepository {
  def deleteByUserId(id: Long): Future[Int] = {
    runCommand(userRoles.filter(_.userId === id).delete)
  }

  def deleteByRoleId(id: Long): Future[Int] = {
    runCommand(userRoles.filter(_.roleId === id).delete)
  }

  def deleteByUserAndRoleId(userId: Long, roleId: Long): Future[Int] = {
    runCommand(userRoles.filter(userRole => userRole.roleId === roleId &&
      userRole.userId === userId).delete)
  }

  def getByUserAndRoleId(userId: Long, roleId: Long): Future[Option[UserRole]] = {
    runCommand(userRoles.filter(userRole => userRole.userId === userId &&
      userRole.roleId === roleId).result).map(_.headOption)
  }

  def getByUserId(userId: Long): Future[Seq[UserRole]] = {
    runCommand(userRoles.filter(_.userId === userId).result)
  }

  def getByRoleId(roleId: Long): Future[Seq[UserRole]] = {
    runCommand(userRoles.filter(_.roleId === roleId).result)
  }

  def getRolesByUserId(userId: Long) : Future[Seq[(Role, UserRole)]] = {
    val join = userRoles
      .join(users).on(_.userId === _.id)
      .join(roles).on(_._1.roleId === _.id)
      .filter(_._1._2.id === userId)
      .result

    runCommand(join)
      .map(_.map(ele => (ele._2, ele._1._1)))
  }

  def create(userRole: UserRole): Future[Int] = {
    val query = for {
      addCount <- userRoles += userRole
    } yield addCount

    runCommand(query)
  }

  def createMultiple(userRoles: Seq[UserRole]): Future[Int] = {
    val query = for {
      addCount <- this.userRoles ++= userRoles
    } yield addCount

    runCommand(query).map(addedCount => {
      addedCount.getOrElse(0)
    }).recover {
      case e: Exception => println("Ex: " + e.getLocalizedMessage); 0
    }
  }
}
