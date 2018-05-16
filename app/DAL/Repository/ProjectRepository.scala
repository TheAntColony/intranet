package DAL.Repository

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject._
import DAL.Models._
import DAL.Traits.IProjectRepository

class ProjectRepository @Inject()() extends BaseRepository() with IProjectRepository {
  def create(project: Project): Future[Option[Long]] = {
    val userIdQuery = (projects returning projects.map(_.id)) += project
    runCommand(userIdQuery).map(userId => {
       Some(userId)
    }).recover {
      case _: Exception => None
    }
  }

  def addSkills(project: Project, skills: Seq[Long]): Future[Option[Long]] = {
    val query = for {
      projectSKillsM <- projectSkills ++= skills.map(skill =>
        ProjectSkill(skillId = skill, projectId = project.id))
    } yield projectSKillsM

    runCommand(query).map(projectSkillsAdded => {
      Some(projectSkillsAdded.get.asInstanceOf[Long])
    }).recover {
      case e: Exception => println("Ex: " + e.getLocalizedMessage); None
    }
  }

  def update(project: Project) : Future[Option[Project]] = {
    val mapUpdateAction = projects.filter(_.id === project.id)
      .map(dbProject => (dbProject.description, dbProject.name, dbProject.startDate, dbProject.endDate))
      .update( (project.description, project.name, project.startDate, project.endDate))

    runCommand(mapUpdateAction)
      .map(updateCount => {
        if (updateCount <= 0) None
        else Some(project)
      })
      .recover {
        case _ : Exception => None
      }
  }

  def delete(id: Long): Future[Int] = {
    runCommand(projects.filter(_.id === id).delete)
  }

  def getById(id: Long): Future[Option[Project]] = {
    runCommand(projects.filter(_.id === id).result).map(_.headOption)
  }

  def getWithOffsetAndLimit(offset: Long, limit: Long): Future[Seq[Project]] = {
    runCommand(projects.drop(offset).take(limit).result)
  }

  def get: Future[Seq[(Project, Seq[Skill])]] = {
    val crossJoin = (for {
      (project, opSkills) <- projects join (projectSkills join skills on (_.skillId === _.id)) on (_.id === _._1.projectId)
    } yield(project, opSkills._2)).result

    runCommand(crossJoin).map(projectsSkillsMapping => {
      projectsSkillsMapping
        .groupBy(_._1)
        .map(ele => (ele._1, ele._2.map(_._2)))
        .toSeq
        .sortWith(_._1.id < _._1.id)
    }).recover {
      case _: Exception => Seq.empty
    }
  }
}

/*val crossJoin = (for {
  (project, opSkills) <- projects join (projectSkills join skills on (_.skillId === _.id)) on (_.id === _._1.projectId) filter(_._1.id === id)
} yield(project, opSkills._2)).result

val sa = runCommand(crossJoin).map(projectWithSkills => {
  projectWithSkills.groupBy(_._1)
    .map(ele => (ele._1, ele._2.map(_._2)))
    .headOption
})*/

/*val join = projectSkills
  .join(skills).on(_.skillId === _.id)
  .join(projects).on(_._1.skillId === _.id)
  .filter(_._2.id === id)
  .result

runCommand(join)
  .map(ps => {
    ps.groupBy(_._2)
      .map(ele => (ele._1, ele._2.map(_._1._2)))
      .headOption
  })*/