package DAL.TableMapping

import slick.jdbc.PostgresProfile.api._
import DAL.Models.UserRole

class UserRoleTable(tag: Tag) extends Table[UserRole](tag, "user_role") {
  val users = TableQuery[UserTable]
  val roles = TableQuery[RoleTable]

  def roleId = column[Long]("roleId")
  def role = foreignKey("roleId_fk", roleId, roles)(_.id,
    onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
  def userId:Rep[Long] = column[Long]("userId")
  def user = foreignKey("userId_fk", userId, users)(_.id)

  override def * =
    (roleId, userId) <>(UserRole.tupled, UserRole.unapply)
}