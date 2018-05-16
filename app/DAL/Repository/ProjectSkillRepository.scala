package DAL.Repository

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject._
import DAL.Models._
import DAL.Traits._

class ProjectSkillRepository @Inject()() extends BaseRepository() with IProjectSkillRepository {
  def deleteBySkillId(id: Long): Future[Int] = {
    runCommand(projectSkills.filter(_.skillId === id).delete)
  }

  def deleteByProjectId(id: Long): Future[Int] = {
    runCommand(projectSkills.filter(_.projectId === id).delete)
  }

  def deleteByProjectAndSkillId(skillId: Long, projectId: Long): Future[Int] = {
    runCommand(projectSkills.filter(userProject => userProject.projectId === projectId &&
      userProject.skillId === skillId).delete)
  }

  def getByProjectAndSkillId(projectId: Long, skillId: Long): Future[Option[ProjectSkill]] = {
    runCommand(projectSkills.filter(userProject => userProject.projectId === projectId &&
      userProject.skillId === skillId).result).map(_.headOption)
  }

  def getBySkillId(skillId: Long): Future[Seq[ProjectSkill]] = {
    runCommand(projectSkills.filter(_.skillId === skillId).result)
  }

  def getByProjectId(projectId: Long): Future[Seq[ProjectSkill]] = {
    runCommand(projectSkills.filter(_.projectId === projectId).result)
  }

  def getSkillsByProjectId(projectId: Long) : Future[Seq[Skill]] = {
    val join = projectSkills
      .join(projects).on(_.projectId === _.id)
      .join(skills).on(_._1.skillId === _.id)
      .filter(_._1._2.id === projectId)
      .result

    runCommand(join)
      .map(_.map(_._2))
  }

  def create(userProject: ProjectSkill): Future[Int] = {
    val query = for {
      addCount <- projectSkills += userProject
    } yield addCount
    runCommand(query)
  }

  def createMultiple(projectSkills: Seq[ProjectSkill]): Future[Int] = {
    val query = for {
      addCount <- this.projectSkills ++= projectSkills
    } yield addCount

    runCommand(query).map(addedCount => {
      addedCount.getOrElse(0)
    }).recover {
      case e: Exception => println("Ex: " + e.getLocalizedMessage); 0
    }
  }
}
