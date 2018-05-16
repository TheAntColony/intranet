package DAL.TableMapping

import java.sql.Timestamp

import slick.jdbc.PostgresProfile.api._
import DAL.Models.Project

class ProjectTable(tag: Tag) extends Table[Project](tag, "project") {
  val projectSkill = TableQuery[ProjectSkillTable]
  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def name = column[String]("name")
  def description = column[String]("description")
  def url = column[String]("url")
  def startDate = column[Timestamp]("startDate")
  def endDate = column[Option[Timestamp]]("endDate")

  override def * =
    (id, name, description, url, startDate, endDate) <>(Project.tupled, Project.unapply)

  def sk = projectSkill.filter(_.projectId === id).flatMap(_.skillFK)
}