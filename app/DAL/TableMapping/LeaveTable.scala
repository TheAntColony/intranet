package DAL.TableMapping

import java.sql.Timestamp

import slick.jdbc.PostgresProfile.api._
import DAL.Models.Leave

class LeaveTable(tag: Tag) extends Table[Leave](tag, "leave") {
  val leaveCategories = TableQuery[LeaveCategoryTable]
  val users = TableQuery[UserTable]

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def reason = column[String]("reason")
  def startDate = column[Timestamp]("startDate")
  def status = column[String]("status")
  def endDate = column[Timestamp]("endDate")
  def evaluationComment = column[Option[String]]("comment")

  def categoryId = column[Long]("categoryId")
  def category = foreignKey("categoryId_fk", categoryId, leaveCategories)(_.id)
  def userId = column[Long]("userId")
  def user = foreignKey("userId_fk", categoryId, users)(_.id)

  override def * =
    (id, reason, status,startDate, endDate, evaluationComment, categoryId, userId) <>(Leave.tupled, Leave.unapply)
}
