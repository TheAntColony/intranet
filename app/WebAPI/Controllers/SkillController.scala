package WebApi.Controllers

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import BLL.Models._
import BLL.Services._
import DAL.Repository.SkillRepository
import WebApi.Models.RestResponse

@Singleton
class SkillController @Inject()(cc: ControllerComponents,
                                skillService: SkillService,
                                skillRepository: SkillRepository)
  extends AbstractController(cc) {

  def get = Action {
    val skills = skillService.get
    Ok(Json.toJson(new RestResponse(Json.toJson(skills), None).toJson))
  }

  def getById(id: Long) = Action {
    val skill: Option[SkillModel] = skillService.getById(id)
    skill match {
      case skillM: Some[SkillModel] => Ok(Json.toJson(skillM))
      case None => NotFound("No skill with id")
    }
  }

  def post: Action[JsValue] = Action(parse.json) { request =>
    val skillModelValidation = request.body.validate[SkillModel]
    if(skillModelValidation.isSuccess) {
      val skillModel = request.body.as[SkillModel]
      val skillCreateResult = skillService.create(skillModel)
      if(skillCreateResult.isSuccess) Created(Json.toJson(skillCreateResult.result))
      else BadRequest(skillCreateResult.message)
    } else {
      BadRequest("Invalid data sent")
    }
  }

  def delete(id: Long) = Action {
    val deletedUserId: Int = skillService.delete(id)
    if(deletedUserId == id) {
      Ok("Skill deleted")
    } else {
      NotFound("No skill with id for deletion")
    }
  }
}