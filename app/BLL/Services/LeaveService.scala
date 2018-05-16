package BLL.Services

import BLL.Converters
import BLL.Enums.LeaveStatusEnum
import BLL.Models.{LeaveModel, OperationResult}
import DAL.Traits.{ILeaveCategoryRepository, ILeaveRepository, IUserRepository}
import javax.inject.Inject

import scala.concurrent.Await
import scala.concurrent.duration._

class LeaveService @Inject()(private val leaveRepository: ILeaveRepository,
                             private val userRepository: IUserRepository,
                             private val leaveCategoryRepository: ILeaveCategoryRepository) {
  private val timeoutDuration = 3.seconds


  def create(leaveM: LeaveModel): OperationResult[Long] = {
    // Converter will assign pending status, I assign here to None if somebody tries to send other status on create
    leaveM.status = None

    val noReferenceErrorResult = OperationResult(isSuccess = false,
      message = "Invalid category or user", result = 0L)
    leaveM.category.fold(noReferenceErrorResult)(category => {
      leaveM.user.fold(noReferenceErrorResult)(user => {
        val dbVacation = Converters.leaveModelToLeave(leaveM, category.id.getOrElse(0L),
          user.id.getOrElse(0L))

        val addVacationF = leaveRepository.create(dbVacation)
        val addVacation = Await.result(addVacationF, timeoutDuration)
        addVacation match {
          case Some(0) | None => OperationResult(isSuccess = false,
            message = "No leave created", result = 0L)
          case Some(leaveId) => OperationResult(isSuccess = true,
            message = "Leave created", result = leaveId)
        }
      })
    })
  }

  def update(leaveId: Long, leaveModel: LeaveModel):
    OperationResult[Option[LeaveModel]] = {
    val noReferenceErrorResult = OperationResult[Option[LeaveModel]](isSuccess = false,
      message = "Invalid category or user", result = None)

    leaveModel.category.fold(noReferenceErrorResult)(category => {
      leaveModel.user.fold(noReferenceErrorResult)(user => {
        val dbLeave = Converters.leaveModelToLeave(leaveModel,
          category.id.getOrElse(0L), user.id.getOrElse(0L))

        val updateUserResult = Await.result(leaveRepository.update(dbLeave), timeoutDuration)
        updateUserResult match {
          case Some(_) => OperationResult[Option[LeaveModel]](isSuccess = true,
            "Leave updated", Some(leaveModel))
          case None => OperationResult[Option[LeaveModel]](isSuccess = false,
            "Leave not updated", None)
        }
      })
    })
  }

  def delete(id: Long): Int = {
    Await.result(leaveRepository.delete(id), timeoutDuration)
  }

  def getById(id: Long): Option[LeaveModel] = {
    val oLeave = Await.result(leaveRepository.getById(id), timeoutDuration)

    oLeave match {
      case Some(leave) =>
        val dbCategory = Await.result(leaveCategoryRepository.getById(leave.categoryId), timeoutDuration)
        dbCategory match {
          case Some(category) =>
            val categoryModel = Converters.leaveCategoryToLeaveCategoryModel(category)
            val oUser = Await.result(userRepository.getById(leave.userId), timeoutDuration)
            oUser match {
              case Some(user) =>
                val userModel = Converters.userToUserModel(user, None, None ,None, None)
                Some(Converters.leaveToLeaveModel(leave, Some(categoryModel), Some(userModel)))
              case None => Some(Converters.leaveToLeaveModel(leave, Some(categoryModel), None))
            }
          case None => Some(Converters.leaveToLeaveModel(leave, None, None))
        }
      case None => None
    }
  }

  def getPending: Seq[LeaveModel] = {
    val leaves = Await.result(leaveRepository.get, timeoutDuration)
    leaves
      .filter(leave => leave._1.status == LeaveStatusEnum.Pending.toString)
      .map(leaveWithCategoryAndUser => {
        val leaveCategoryModel = Converters.leaveCategoryToLeaveCategoryModel(leaveWithCategoryAndUser._2)
        val userModel = Converters.userToUserModel(leaveWithCategoryAndUser._3, None, None, None, None)
        Converters.leaveToLeaveModel(leaveWithCategoryAndUser._1, Some(leaveCategoryModel), Some(userModel))
    })
  }

  def get: Seq[LeaveModel] = {
    val leaves = Await.result(leaveRepository.get, timeoutDuration)
    leaves.map(leaveWithCategoryAndUser => {
      val leaveCategoryModel = Converters.leaveCategoryToLeaveCategoryModel(leaveWithCategoryAndUser._2)
      val userModel = Converters.userToUserModel(leaveWithCategoryAndUser._3, None, None, None, None)
      Converters.leaveToLeaveModel(leaveWithCategoryAndUser._1, Some(leaveCategoryModel), Some(userModel))
    })
  }

  def getTotalPending: Int = {
    Await.result(leaveRepository.getTotalByStatus(Some(LeaveStatusEnum.Pending.toString)), timeoutDuration)
  }
}
