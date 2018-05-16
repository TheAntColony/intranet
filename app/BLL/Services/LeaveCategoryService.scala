package BLL.Services

import BLL.Converters
import BLL.Models.{LeaveCategoryModel, OperationResult}
import DAL.Repository.LeaveCategoryRepository
import javax.inject.Inject

import scala.concurrent.Await
import scala.concurrent.duration._

class LeaveCategoryService @Inject()(leaveCategoryRepository: LeaveCategoryRepository) {
  private val timeoutDuration = 2.seconds

  def create(leaveCategoryM: LeaveCategoryModel): OperationResult[Long] = {
    val dbSkill = Converters.leaveCategoryModelToLeaveCategory(leaveCategoryM)

    val addSkillF = leaveCategoryRepository.create(dbSkill)
    val addSkill = Await.result(addSkillF, timeoutDuration)
    addSkill match {
      case Some(0) | None => OperationResult(isSuccess = false,
        message = "No leave category created", result = 0L)
      case Some(categoryId) => OperationResult(isSuccess = true,
        message = "Leave category created", result = categoryId)
    }
  }

  def delete(id: Long): Int = {
    Await.result(leaveCategoryRepository.delete(id), timeoutDuration)
  }

  def getById(id: Long): Option[LeaveCategoryModel] = {
    val opSkill = Await.result(leaveCategoryRepository.getById(id), timeoutDuration)

    opSkill match {
      case Some(leaveCategory) => Some(Converters.leaveCategoryToLeaveCategoryModel(leaveCategory))
      case None => None
    }
  }

  def get: Seq[LeaveCategoryModel] = {
    val skills = Await.result(leaveCategoryRepository.get, timeoutDuration)
    skills.map(Converters.leaveCategoryToLeaveCategoryModel)
  }
}
