package BLL.Services

import BLL.Converters
import BLL.Models.{InterestingInfoModel, OperationResult}
import DAL.Repository.UserInterestingInfoRepository
import javax.inject.Inject

import scala.concurrent.Await
import scala.concurrent.duration._

class UserInterestingInfoService @Inject()(interestingInfoRepository: UserInterestingInfoRepository) {
  private val timeoutDuration = 2.seconds

  def create(userId: Long, infoModel: InterestingInfoModel): OperationResult[Long] = {
    val info = Converters.interestingInfoModelToInterestingInfo(infoModel, userId)

    val addInfo = Await.result(interestingInfoRepository.create(info), timeoutDuration)
    addInfo match {
      case Some(0) | None => OperationResult(isSuccess = false,
        message = "Interesting info not created", result = 0L)
      case Some(infoId) => OperationResult(isSuccess = true,
        message = "Interesting info created", result = infoId)
    }
  }

  def delete(id: Long): Int = {
    Await.result(interestingInfoRepository.delete(id), timeoutDuration)
  }

  def getById(id: Long): Option[InterestingInfoModel] = {
    val opInfo = Await.result(interestingInfoRepository.getById(id), timeoutDuration)

    opInfo match {
      case Some(info) => Some(Converters.interestingInfoToInterestingInfoModel(info))
      case None => None
    }
  }

  def get: Seq[InterestingInfoModel] = {
    val infos = Await.result(interestingInfoRepository.get, timeoutDuration)
    infos.map(Converters.interestingInfoToInterestingInfoModel)
  }
}
