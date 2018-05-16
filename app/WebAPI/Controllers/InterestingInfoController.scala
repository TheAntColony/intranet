package WebApi.Controllers

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import BLL.Models._
import BLL.Services._
import DAL.Repository.UserInterestingInfoRepository
import WebApi.Models.RestResponse

@Singleton
class InterestingInfoController @Inject()(cc: ControllerComponents,
                                interestingInfoService: UserInterestingInfoService,
                                skillRepository: UserInterestingInfoRepository)
  extends AbstractController(cc) {

  def get = Action {
    val skills = interestingInfoService.get
    Ok(Json.toJson(new RestResponse(Json.toJson(skills), None).toJson))
  }

  def getById(id: Long) = Action {
    val skill: Option[InterestingInfoModel] = interestingInfoService.getById(id)
    skill match {
      case skillM: Some[InterestingInfoModel] => Ok(Json.toJson(skillM))
      case None => NotFound("No interesting info with id")
    }
  }

  def post(userId: Long): Action[JsValue] = Action(parse.json) { request =>
    val skillModelValidation = request.body.validate[InterestingInfoModel]
    if(skillModelValidation.isSuccess) {
      val interestingInfoModel = request.body.as[InterestingInfoModel]
      val skillCreateResult = interestingInfoService.create(userId, interestingInfoModel)
      if(skillCreateResult.isSuccess) Created(Json.toJson(skillCreateResult.result))
      else BadRequest(skillCreateResult.message)
    } else {
      BadRequest("Invalid data sent")
    }
  }

  def delete(id: Long) = Action {
    val deletedUserId: Int = interestingInfoService.delete(id)
    if(deletedUserId == id) {
      Ok("Interesting info deleted")
    } else {
      NotFound("No interesting info with id for deletion")
    }
  }
}