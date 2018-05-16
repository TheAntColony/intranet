package WebApi.Controllers

import BLL.Services.AuthenticationService
import WebApi.Models._
import javax.inject._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import WebApi.Utilities.{JWTAuthentication, JwtUtility}

@Singleton
class AccountController @Inject()(cc: ControllerComponents,
                                  jwtAuthentication: JWTAuthentication,
                                  jwtUtility: JwtUtility,
                                  authService: AuthenticationService)
  extends AbstractController(cc) {

  def index = jwtAuthentication { implicit request =>
    println("User auth info")
    println(request.userInfo)
    Ok("User authenticated")
  }

  def generateToken: Action[JsValue] = Action(parse.json) { request =>
    val userBodyValidation = request.body.validate[UserCredentials]
    if(userBodyValidation.isSuccess) {
      val userCredentials = request.body.as[UserCredentials]
      val tokenPayload: Option[String] = authService.generateTokenPayload(userCredentials)
      tokenPayload match {
        case Some(payload) => Ok(Json.toJson(jwtUtility.createToken(payload)))
        case None => NotFound
      }
    } else {
      BadRequest("Invalid credentials")
    }
  }


}
