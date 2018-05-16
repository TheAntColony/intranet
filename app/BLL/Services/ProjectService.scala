package BLL.Services

import BLL.Converters
import javax.inject._

import scala.concurrent.duration._
import scala.concurrent.Await
import BLL.Models._
import DAL.Models.{Project, ProjectSkill, Skill}
import DAL.Traits._

class ProjectService @Inject()(private val projectRepository: IProjectRepository,
                               private val projectSkillRepository: IProjectSkillRepository) {
  private val timeoutDuration = 3.seconds

  def create(projectM: ProjectModel): OperationResult[Long] = {
      val dbProject = Converters.projectModelToProject(projectM)

      val addProjectF = projectRepository.create(dbProject)
      val addProject = Await.result(addProjectF, timeoutDuration)
      addProject match {
        case Some(0L) | None => OperationResult(isSuccess = false,
          message = "Project not created", result = 0L)
        case Some(projectId) => OperationResult(isSuccess = true,
          message = "Project created", result = projectId)
      }
  }

  def addSkills(projectId: Long, skills: Seq[Long]): OperationResult[Int] = {
    val oProject = Await.result(projectRepository.getById(projectId), timeoutDuration)

    oProject match {
      case Some(_) =>
        val projectSkills = skills.map(skillId => ProjectSkill(skillId = skillId, projectId = projectId))
        val addProjectSkill = Await.result(projectSkillRepository.createMultiple(projectSkills), timeoutDuration)
        if(addProjectSkill <= 0) OperationResult(isSuccess = false,
            message = "No project skills added", result = 0)
        else OperationResult(isSuccess = true,
          message = "Project skills added", result = addProjectSkill)
      case None => OperationResult(isSuccess = false,
        message = "No project with specified id", result = 0)
    }
  }

  def delete(id: Long): Int = {
    Await.result(projectRepository.delete(id), timeoutDuration)
  }

  def getById(id: Long): Option[ProjectModel] = {
    val oProject:Option[Project] = Await.result(projectRepository.getById(id), timeoutDuration)

    oProject match {
      case Some(project) =>
        val dbSkills: Seq[Skill] = Await.result(projectSkillRepository.getSkillsByProjectId(project.id), timeoutDuration)

        val skills = dbSkills.map(Converters.skillToSkillModel(_, None))
        Some(Converters.projectToProjectModel(project, Some(skills)))
      case None => None
    }
  }

  def get: Seq[ProjectModel] = {
    val projects = Await.result(projectRepository.get, timeoutDuration)
    projects.map(project => {
      val skills = project._2.map(Converters.skillToSkillModel(_, None))
      Converters.projectToProjectModel(project._1, Some(skills))
    })
  }
}
