package WebApi.Models

import BLL.Models.UserModel
import play.api.mvc.{Request, WrappedRequest}

case class UserRequest[A](userInfo: UserModel, request: Request[A]) extends WrappedRequest[A](request)
