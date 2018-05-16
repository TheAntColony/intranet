package DAL.Repository

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject._
import DAL.Models._
import DAL.Traits.ISkillRepository

class SkillRepository @Inject()() extends BaseRepository() with ISkillRepository {
  def create(skill: Skill): Future[Option[Long]] = {
    val skillIdQuery = (skills returning skills.map(_.id)) += skill
    runCommand(skillIdQuery).map(skillId => {
       Some(skillId)
    }).recover {
      case _: Exception => None
    }
  }

  def delete(id: Long): Future[Int] = {
    runCommand(skills.filter(_.id === id).delete)
  }

  def getById(id: Long): Future[Option[Skill]] = {
    runCommand(skills.filter(_.id === id).result).map(_.headOption)
  }

  def get: Future[Seq[Skill]] = {
    runCommand(skills.result)
  }
}
