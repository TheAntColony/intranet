package WebApi.Models

import play.api.libs.json.{Json, Reads, Writes}

case class UserCredentials(email: String, password: String, rememberMe: Boolean)

object UserCredentials {
  implicit val jsonFormat = Json.format[UserCredentials]
  implicit val credentialsRead: Reads[UserCredentials] = Json.reads[UserCredentials]
  implicit val credentialsWrite: Writes[UserCredentials] = Json.writes[UserCredentials]
}
