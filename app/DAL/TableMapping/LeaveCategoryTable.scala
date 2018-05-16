package DAL.TableMapping

import slick.jdbc.PostgresProfile.api._
import DAL.Models.LeaveCategory

class LeaveCategoryTable(tag: Tag) extends Table[LeaveCategory](tag, "leave_category") {
  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def name = column[String]("name", O.Unique)

  override def * =
    (id, name) <>(LeaveCategory.tupled, LeaveCategory.unapply)
}
