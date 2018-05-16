package DAL.Traits

import DAL.Models.Skill

import scala.concurrent.Future

trait ISkillRepository {
  def create(skill: Skill): Future[Option[Long]]

  def delete(id: Long): Future[Int]

  def getById(id: Long): Future[Option[Skill]]

  def get: Future[Seq[Skill]]
}
