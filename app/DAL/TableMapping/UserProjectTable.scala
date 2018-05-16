package DAL.TableMapping

import slick.jdbc.PostgresProfile.api._
import DAL.Models.UserProject

class UserProjectTable(tag: Tag) extends Table[UserProject](tag, "user_project") {
  val projects = TableQuery[ProjectTable]
  val users = TableQuery[UserTable]

  def userId :Rep[Long] = column[Long]("userId")
  def user = foreignKey("userId_fk", userId, users)(_.id,
    onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
  def projectId: Rep[Long] = column[Long]("projectId")
  def project = foreignKey("projectId_fk", projectId, projects)(_.id)

  override def * =
    (userId, projectId) <>(UserProject.tupled, UserProject.unapply)
}
