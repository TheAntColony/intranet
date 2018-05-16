package DAL.Repository

import scala.concurrent.Future
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject._
import DAL.Models._
import DAL.Traits._

class UserProjectRepository @Inject()() extends BaseRepository() with IUserProjectRepository {
  def deleteByUserId(id: Long): Future[Int] = {
    runCommand(userProjects.filter(_.userId === id).delete)
  }

  def deleteByProjectId(id: Long): Future[Int] = {
    runCommand(userProjects.filter(_.projectId === id).delete)
  }

  def deleteByUserAndProjectId(userId: Long, projectId: Long): Future[Int] = {
    runCommand(userProjects.filter(userProject => userProject.projectId === projectId &&
      userProject.userId === userId).delete)
  }

  def getByUserAndProjectId(userId: Long, projectId: Long): Future[Option[UserProject]] = {
    runCommand(userProjects.filter(userProject => userProject.userId === userId &&
      userProject.projectId === projectId).result).map(_.headOption)
  }

  def getByUserId(userId: Long): Future[Seq[UserProject]] = {
    runCommand(userProjects.filter(_.userId === userId).result)
  }

  def getByProjectId(projectId: Long): Future[Seq[UserProject]] = {
    runCommand(userProjects.filter(_.projectId === projectId).result)
  }

  def getProjectsByUserId(userId: Long) : Future[Seq[(Project, Seq[Skill])]] = {
    val join = userProjects
      .join(users).on(_.userId === _.id)
      .join(projects).on(_._1.projectId === _.id)
      .filter(_._1._2.id === userId)
      .joinLeft(projectSkills).on(_._1._1.projectId === _.projectId)
      .joinLeft(skills).on(_._2.map(_.skillId) === _.id)
      .result

    runCommand(join)
      .map(_.groupBy(_._1._1._2)
          .map(ele => (ele._1, ele._2.flatten(_._2))) // Flatten will remove all None from Option and get will then only get skill values
          .toSeq)
  }

  def create(userProject: UserProject): Future[Int] = {
    val query = for {
      addCount <- userProjects += userProject
    } yield addCount
    runCommand(query)
  }

  def createMultiple(userProjects: Seq[UserProject]): Future[Int] = {
    val query = for {
      addCount <- this.userProjects ++= userProjects
    } yield addCount

    runCommand(query).map(addedCount => {
      addedCount.getOrElse(0)
    }).recover {
      case e: Exception => println("Ex: " + e.getLocalizedMessage); 0
    }
  }
}
