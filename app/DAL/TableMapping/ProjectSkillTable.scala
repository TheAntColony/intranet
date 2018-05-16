package DAL.TableMapping

import slick.jdbc.PostgresProfile.api._
import DAL.Models.ProjectSkill

class ProjectSkillTable(tag: Tag) extends Table[ProjectSkill](tag, "project_skill") {
  val projects = TableQuery[ProjectTable]
  val skills = TableQuery[SkillTable]

  def projectId :Rep[Long] = column[Long]("projectId")
  def projectFK = foreignKey("projectId_fk", projectId, projects)(_.id,
    onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
  def skillId: Rep[Long] = column[Long]("skillId")
  def skillFK = foreignKey("skillId_fk", skillId, skills)(_.id)

  override def * =
    (skillId, projectId) <>(ProjectSkill.tupled, ProjectSkill.unapply)
}