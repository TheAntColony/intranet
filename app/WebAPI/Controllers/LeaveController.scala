package WebApi.Controllers

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import BLL.Models._
import BLL.Services._
import DAL.Repository.LeaveRepository
import WebApi.Models.RestResponse
import WebApi.Utilities.JWTAuthentication

@Singleton
class LeaveController @Inject()(cc: ControllerComponents,
                                jwtAuthentication: JWTAuthentication,
                                leaveService: LeaveService,
                                vacationRepository: LeaveRepository)
  extends AbstractController(cc) {

  def get = Action {
    val leaves:Seq[LeaveModel] = leaveService.get
    Ok(RestResponse(Json.toJson(leaves), None).toJson)
  }

  def getById(id: Long) = Action {
    val leaveM: Option[LeaveModel] = leaveService.getById(id)
    leaveM match {
      case leave: Some[LeaveModel] => Ok(Json.toJson(leave))
      case None => NotFound("No leave with id")
    }
  }

  def getTotalPending = Action {
    Ok(leaveService.getTotalPending.toString)
  }


  def post: Action[JsValue] = jwtAuthentication(parse.json) { request =>
    val validateVacationBody = request.body.validate[LeaveModel]

    if(validateVacationBody.isSuccess) {
      val leaveModel = request.body.as[LeaveModel]
      val createLeaveResult = leaveService.create(leaveModel)

      if(createLeaveResult.isSuccess) Created(Json.toJson(createLeaveResult.result))
      else BadRequest(createLeaveResult.message)
    } else {
      BadRequest("Invalid data sent")
    }
  }

  def update(id: Long): Action[JsValue] = Action(parse.json) { request =>
    val leaveBodyValidation = request.body.validate[LeaveModel]
    if(leaveBodyValidation.isSuccess) {
      val leaveFromBody = request.body.as[LeaveModel]

      val updateResult: OperationResult[Option[LeaveModel]] =
        leaveService.update(id, leaveFromBody)

      if (updateResult.isSuccess) Ok(Json.toJson(updateResult.result))
      else BadRequest(updateResult.message)
    } else {
      BadRequest("Invalid data sent")
    }
  }

  def getPending = Action {
    val leaves:Seq[LeaveModel] = leaveService.getPending
    Ok(RestResponse(Json.toJson(leaves), None).toJson)
  }

  def delete(id: Long) = Action {
    val deletedLeaveId: Int = leaveService.delete(id)

    if(deletedLeaveId == id) Ok("Leave deleted")
    else NotFound("No leave with id for deletion")
  }

}