package DAL.Traits

import DAL.Models.{ProjectSkill, Skill}

import scala.concurrent.Future

trait IProjectSkillRepository {
  def deleteByProjectId(id: Long): Future[Int]
  def deleteBySkillId(id: Long): Future[Int]
  def deleteByProjectAndSkillId(projectId: Long, skillId: Long): Future[Int]
  def getByProjectAndSkillId(projectId: Long, skillId: Long): Future[Option[ProjectSkill]]
  def getByProjectId(projectId: Long): Future[Seq[ProjectSkill]]
  def getBySkillId(skillId: Long): Future[Seq[ProjectSkill]]
  def getSkillsByProjectId(projectId: Long) : Future[Seq[Skill]]
  def create(projectSkill: ProjectSkill): Future[Int]
  def createMultiple(skills: Seq[ProjectSkill]): Future[Int]
}
