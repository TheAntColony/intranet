package WebApi.Utilities

import WebApi.Models.JwtToken
import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import play.api.Configuration
import javax.inject._

class JwtUtility @Inject() (config: Configuration){
  private val jwtSecretKey = config.get[String]("jwt.secretKey")
  private val jwtSecretAlgo = config.get[String]("jwt.algorithm")
  private val jwtSchema = config.get[String]("jwt.schema")

  def createToken(payload: String): JwtToken = {
    val header = JwtHeader(jwtSecretAlgo)
    val claimsSet = JwtClaimsSet(payload)
    val token = JsonWebToken(header, claimsSet, jwtSecretKey)
    JwtToken(token, jwtSchema, None)
  }

  def isValidToken(jwtToken: String): Boolean =
    JsonWebToken.validate(jwtToken, jwtSecretKey)

  def decodePayload(jwtToken: String): Option[String] =
    jwtToken match {
      case JsonWebToken(header, claimsSet, signature) => Option(claimsSet.asJsonString)
      case _ => None
    }
}
