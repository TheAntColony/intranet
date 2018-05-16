package BLL

import BLL.Enums.RoleEnum
import DAL.Models.Role
import DAL.Traits.IRoleRepository
import javax.inject.Inject
import javax.inject.Singleton
import play.api.Configuration

import scala.concurrent.Await
import scala.concurrent.duration._

@Singleton
class StartupService @Inject()(private val roleRepository: IRoleRepository,
                               configuration: Configuration) {
  //The code that needs to be executed
  println("Called on startup")

  this.createRoles()


  def createRoles(): Boolean = {
    val roleNames: Seq[RoleEnum.Value] = RoleEnum.values.toSeq // configuration.get[Seq[String]]("roles")

    val roles: Seq[Role] = Await.result(roleRepository.get, 2.seconds)
    // Check if there are roles in db if it is empty or there is new role in configuration add those roles, else do nothing
    if(roles.isEmpty || roles.length < roleNames.length) {
      for(i <- roles.length until roleNames.length) {
        Await.result(roleRepository.create(Role(0L, roleNames(i).toString )), 2.seconds)
      }

      true
    } else {
      false
    }
  }
}

