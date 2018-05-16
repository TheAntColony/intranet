package WebApi.Controllers

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import BLL.Models._
import BLL.Services._
import DAL.Repository.LeaveCategoryRepository
import WebApi.Models.RestResponse

@Singleton
class LeaveCategoryController @Inject()(cc: ControllerComponents,
                                leaveCategoryService: LeaveCategoryService,
                                leaveCategoryRepository: LeaveCategoryRepository)
  extends AbstractController(cc) {

  def get = Action {
    val vacations:Seq[LeaveCategoryModel] = leaveCategoryService.get
    Ok(new RestResponse(Json.toJson(vacations), None).toJson)
  }

  def getById(id: Long) = Action {
    val leaveCategoryM: Option[LeaveCategoryModel] = leaveCategoryService.getById(id)
    leaveCategoryM match {
      case leave: Some[LeaveCategoryModel] => Ok(Json.toJson(leave))
      case None => NotFound("No vacation with id")
    }
  }

  def post: Action[JsValue] = Action(parse.json) { request =>
    val validateLeaveBody = request.body.validate[LeaveCategoryModel]
    if(validateLeaveBody.isSuccess) {
      val leaveCategoryModel = request.body.as[LeaveCategoryModel]
      val leaveCreateResult = leaveCategoryService.create(leaveCategoryModel)

      if(leaveCreateResult.isSuccess) Created(Json.toJson(leaveCreateResult.result))
      else BadRequest(leaveCreateResult.message)
    } else {
      BadRequest("Invalid data sent")
    }
  }

  def delete(id: Long) = Action {
    val deletedLeaveCategoryId: Int = leaveCategoryService.delete(id)
    if(deletedLeaveCategoryId == id) {
      Ok("Leave category deleted")
    } else {
      NotFound("No leave category with id for deletion")
    }
  }

}