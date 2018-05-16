package BLL.Services

import DAL.Models.User
import javax.inject._
import DAL.Traits._
import WebApi.Models.{UserCredentials, UserJwtPayload}
import org.mindrot.jbcrypt.BCrypt
import play.api.libs.json.{Json, Writes}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class AuthenticationService @Inject()(ussr: IUserRepository) {
  private val userRepository: IUserRepository = ussr
  private implicit val jwtPayloadWrites: Writes[UserJwtPayload] = Json.writes[UserJwtPayload]
  private val timeoutDuration = 2.seconds

  def checkUserCredentials(email: String, password: String): Boolean = {
    val userF: Future[Option[User]] = userRepository.getByEmail(email)
    val user: Option[User] = Await.result(userF, timeoutDuration)
    user match  {
      case Some(dbUser) => validatePassword(password, dbUser.password.getOrElse(""))
      case None => false
    }
  }

  def generateTokenPayload(credentials: UserCredentials): Option[String] = {
    val userAuthorized: Boolean = checkUserCredentials(credentials.email, credentials.password)
    if (userAuthorized) {
      val userF: Future[Option[User]] = ussr.getByEmail(credentials.email)
      val user: Option[User] = Await.result(userF, timeoutDuration)
      user match  {
        case Some(sUser) => Some(Json.toJson(UserJwtPayload(sUser.email, sUser.id, "Customer")).toString)
        case None => None
      }
    } else None
  }

  def validatePassword(password: String, passwordHash: String): Boolean = {
    BCrypt.checkpw(password, passwordHash)
  }

  def hashPassword(password: String): String = {
    BCrypt.hashpw(password, BCrypt.gensalt())
  }
}
