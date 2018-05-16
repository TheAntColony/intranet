package WebApi.Utilities

import BLL.Converters
import javax.inject.Inject
import play.api.libs.json._
import play.api.mvc._
import play.api.mvc.Results._

import scala.concurrent.{ExecutionContext, Future}
import DAL.Models._
import DAL.Repository.UserRepository
import WebApi.Models.{UserJwtPayload, UserRequest}
import play.api.http.HttpVerbs

import scala.concurrent._
import scala.concurrent.duration._

class JWTAuthentication @Inject()(playBodyParsers: PlayBodyParsers,
                                  userRepository: UserRepository,
                                  jwtUtility: JwtUtility)
                                 (implicit val executionContext: ExecutionContext)
  extends ActionBuilder[UserRequest, AnyContent]
    with HttpVerbs {

  val parser: BodyParser[AnyContent] = playBodyParsers.default

  override def invokeBlock[A](request: Request[A],
                              block: UserRequest[A] => Future[Result]): Future[Result] = {

    val jwtTokenAuth = request.headers.get("Authorization").getOrElse("")
    val jwtToken = jwtTokenAuth.replaceAll("(Bearer|bearer)" + " ", "")

    if (jwtUtility.isValidToken(jwtToken)) {
      jwtUtility.decodePayload(jwtToken).fold {
        Future.successful(Unauthorized("Invalid credential wrong payload"))
      } { payload =>
        val userCredentialsValidation: JsResult[UserJwtPayload] = Json.parse(payload).validate[UserJwtPayload]

        userCredentialsValidation match {
          case jsUserCredentials: JsSuccess[UserJwtPayload] =>
            val userCredentials: UserJwtPayload = jsUserCredentials.get

            val userInfo: Option[User] = Await.result(userRepository.getByEmail(userCredentials.email), 3.seconds)
            println(userInfo)
            userInfo match {
              case Some(user) =>
                if (user.id == userCredentials.userId) {
                  val userModel = Converters.userToUserModel(user, None, None, None, None)
                  block(UserRequest(userModel, request))
                }
                else
                  Future.successful(Unauthorized("Invalid credential, invalid id"))
              case None =>
                Future.successful(Unauthorized("Invalid credential"))
            }
          case _: JsError => Future.successful(Unauthorized("Invalid token payload"))
        }
      }
    } else {
      Future.successful(Unauthorized("Invalid credential, invalid token"))
    }
  }
}
