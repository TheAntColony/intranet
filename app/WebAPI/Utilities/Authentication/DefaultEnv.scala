package WebApi.Utilities.Authentication

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

import DAL.Models.User

/** The default Silhouette Environment.
  */
trait DefaultEnv extends Env {

  /** Identity
    */
  type I = User

  /** Authenticator used for identification.
    *  [[BearerTokenAuthenticator]] could've also been used for REST.
    */
  type A = JWTAuthenticator

}