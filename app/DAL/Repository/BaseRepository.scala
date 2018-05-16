package DAL.Repository

import DAL.TableMapping._
import slick.dbio.{DBIOAction, NoStream}
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

object BaseRepository {
  val db = Database.forConfig("postgresDatabase")
}

class BaseRepository () {
  // We get config by name from application.config where db url is specified
  val users = TableQuery[UserTable]
  val userSkills = TableQuery[UserSkillTable]
  val leaves = TableQuery[LeaveTable]
  val projects = TableQuery[ProjectTable]
  val skills = TableQuery[SkillTable]
  val projectSkills = TableQuery[ProjectSkillTable]
  val usersDetails = TableQuery[UserDetailTable]
  val usersInterestingInfo = TableQuery[UserInterestingInfoTable]
  val leaveCategories = TableQuery[LeaveCategoryTable]
  val userProjects = TableQuery[UserProjectTable]
  val roles = TableQuery[RoleTable]
  val userRoles = TableQuery[UserRoleTable]
  
  def getDatabaseConnection: JdbcProfile#Backend#Database = {
    BaseRepository.db
  }

  def runCommand[R](command: DBIOAction[R, NoStream, Nothing]): Future[R] = {
    /*val db = Database.forConfig("databaseUrl")
    try{
      db.run(command)
    } finally db.close()*/
    BaseRepository.db.run(command)
  }
}
