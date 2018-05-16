package WebApi.Controllers

import javax.inject._

import play.api.mvc._


@Singleton
class HomeController @Inject()(cc: ControllerComponents) 
  extends AbstractController(cc) {

  def index = Action {
    Ok("Welcome to index action")
  }
}
