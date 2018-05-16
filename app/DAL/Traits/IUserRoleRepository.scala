package DAL.Traits

import DAL.Models.{Role, UserRole}

import scala.concurrent.Future

trait IUserRoleRepository {
  def deleteByUserId(id: Long): Future[Int]
  def deleteByRoleId(id: Long): Future[Int]
  def deleteByUserAndRoleId(userId: Long, roleId: Long): Future[Int]
  def getByUserAndRoleId(userId: Long, roleId: Long): Future[Option[UserRole]]
  def getByUserId(userId: Long): Future[Seq[UserRole]]
  def getByRoleId(roleId: Long): Future[Seq[UserRole]]
  def getRolesByUserId(userId: Long) : Future[Seq[(Role, UserRole)]]
  def create(UserRole: UserRole): Future[Int]
  def createMultiple(roles: Seq[UserRole]): Future[Int]
}
