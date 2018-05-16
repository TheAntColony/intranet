package BLL.Services

import javax.inject._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import BLL.Models.{OperationResult, _}
import DAL.Models._
import DAL.Traits._
import BLL.Converters
import BLL.Enums.RoleEnum

class UserService @Inject()(private val userRepository: IUserRepository,
                            private val userDetailRepository: IUserDetailRepository,
                            private val userSkillRepository: IUserSkillRepository,
                            private val userProjectRepository: IUserProjectRepository,
                            private val userRoleRepository: IUserRoleRepository,
                            private val roleRepository: IRoleRepository,
                            private val leaveRepository: ILeaveRepository,
                            private val authService: AuthenticationService) {
  private val timeoutDuration = 2.seconds

  def create(user: UserModel): OperationResult[Long] = {
    user.password.fold(OperationResult[Long](isSuccess = false,
      "No user password provided", 0L))(password => {
      val hashedPassword = authService.hashPassword(password)
      val dbUser = Converters.userModelToUser(user, hashedPassword)

      val addUserResultId = Await.result(userRepository.create(dbUser), timeoutDuration)
      if(addUserResultId > 0L) {
        val dbUserDetail = UserDetail(0L, description = "", country = "", religion = "", height = 0.0,
          weight= 0.0, skin = "", hair = "", gender = "", age = 0, userId = addUserResultId)
        val addUserDetailId = Await.result(userDetailRepository.create(dbUserDetail),
          timeoutDuration).getOrElse(0L)

        val role = Await.result(roleRepository.getByName(RoleEnum.Employee.toString),
            timeoutDuration)
        role match {
          case Some(dbRole) =>
            val userRoleId = Await.result(userRoleRepository
              .create(UserRole(dbRole.id, addUserResultId)), timeoutDuration)

            if (addUserDetailId > 0L && userRoleId > 0L) OperationResult[Long](isSuccess = true,
              "User created", addUserResultId)
            else OperationResult[Long](isSuccess = false,
              "User not created, can not add user details", 0L)
          case None => OperationResult[Long](isSuccess = false,
            "User not created, can not find role", 0L)
        }
      }
      else {
        OperationResult[Long](isSuccess = false, "User not created, try again later", 0L)
      }
    })
  }

  def addSkills(userId: Long, userSkillModels: Seq[UserSkillModel]): OperationResult[Int] = {
    val oUser = Await.result(userRepository.getById(userId), timeoutDuration)

    oUser match {
      case Some(_) =>
        val userSkills = userSkillModels.map(userSkill =>
          UserSkill(skillId = userSkill.skillId, userId = userId,
            level = userSkill.level, yearsExperience = userSkill.yearsExperience))
        val addUserSkillCount = Await.result(userSkillRepository.createMultiple(userSkills), timeoutDuration)

        if(addUserSkillCount <= 0) OperationResult[Int](isSuccess = false,
          "No user skills added", 0)
        else OperationResult[Int](isSuccess = true,
          "User skills added", addUserSkillCount)
      case None => OperationResult[Int](isSuccess = false,
        "No user with specified id", 0)
    }
  }

  def addProjects(userId: Long, projects: Seq[Long]): OperationResult[Int] = {
    val oUser = Await.result(userRepository.getById(userId), timeoutDuration)

    oUser match {
      case Some(_) =>
        val userProjects = projects.map(projectId => UserProject(projectId = projectId, userId = userId))
        val addUserProjectCount = Await.result(userProjectRepository.createMultiple(userProjects), timeoutDuration)
        if(addUserProjectCount <= 0) OperationResult[Int](isSuccess = false,
            "No user leaves added", 0)
        else  OperationResult[Int](isSuccess = true,
          "User leaves added", addUserProjectCount)
      case None => OperationResult[Int](isSuccess = false,
        "No user with specified id", 0)
    }
  }

  def updateDetails(userId: Long, userDetail: UserDetailModel):
      OperationResult[Option[UserDetailModel]] = {
    userDetail.userId = Some(userId)
    val dbUserDetail = Converters.userDetailModelToUserDetail(userDetail)

    val addDetailsF: Future[Option[UserDetail]] = userDetailRepository.update(dbUserDetail)
    val updateUserResult = Await.result(addDetailsF, timeoutDuration)
    updateUserResult match {
      case Some(updatedUserDetail) =>
        val userDetail = Some(Converters.userDetailToUserDetailModel(updatedUserDetail, Some(updatedUserDetail.userId)))
          OperationResult[Option[UserDetailModel]](isSuccess = true, "User detail updated", userDetail)
      case None => OperationResult[Option[UserDetailModel]](isSuccess = false, "User not updated", None)
    }
  }

  def delete(id: Long): Int = {
    val deleteUserF: Future[Int] = userRepository.delete(id)
    Await.result(deleteUserF, timeoutDuration)
  }

  def getById(id: Long): Option[UserModel] = {
    val opUser: Option[User] = Await.result(userRepository.getById(id), timeoutDuration)

    opUser match {
      case Some(dbUser) =>
        val opUserDetail = Await.result(userDetailRepository
          .getByUserId(dbUser.id), timeoutDuration)
        opUserDetail match {
          case Some(uDetail) =>
            val skills = Await.result(userSkillRepository.getSkillsByUserId(dbUser.id), timeoutDuration)
              .map(skillUserSkillTuple => Converters.skillToSkillModel(skillUserSkillTuple._1, Some(skillUserSkillTuple._2)))

            val projects = Await.result(userProjectRepository.getProjectsByUserId(dbUser.id), timeoutDuration)
              .map(projectWithSkills => {
                val projectSkills = projectWithSkills._2.map(Converters.skillToSkillModel(_, None))
                Converters.projectToProjectModel(projectWithSkills._1, Some(projectSkills))
              })

            val leaves = Await.result(leaveRepository.getByUserId(dbUser.id), timeoutDuration)
              .map(leaveWithCategoryTuple => {
                val categoryModel = Converters.leaveCategoryToLeaveCategoryModel(leaveWithCategoryTuple._2)
                Converters.leaveToLeaveModel(leave = leaveWithCategoryTuple._1,
                  category = Some(categoryModel), None)

              })

            val userDetail = Some(Converters.userDetailToUserDetailModel(uDetail, None))
            Some(Converters.userToUserModel(dbUser, userDetail, Some(skills), Some(projects), Some(leaves)))
          case None => None
        }
      case None => None
    }
  }

  def getWithOffsetAndLimit(offset: Long, limit: Long): Seq[UserModel] = {
    if (limit > 1000) {
      return Seq.empty[UserModel]
    }

    val dbUsers = Await.result(userDetailRepository
      .getWithOffsetAndLimit(offset, limit), timeoutDuration)
    dbUsers.map(userWithDetail => {
      val userDetailM = Some(Converters.userDetailToUserDetailModel(userWithDetail._2, None))
      Converters.userToUserModel(userWithDetail._1, userDetailM, None, None, None)
    })
  }

  def getTotal: Int = {
    Await.result(userRepository.getTotal, timeoutDuration)
  }
}


/*object UserForm {
  val form = Form(
    mapping(
      "id" -> longNumber,
      "firstName" -> nonEmptyText,
      "lastName" -> nonEmptyText,
      "email" -> email,
      "username" -> nonEmptyText,
      "password" -> optional(text),
      "userDetails" -> optional[UserDetail](s)
    )(UserModel.apply)(UserModel.unapply)
  )
}*/