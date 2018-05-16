import BLL.StartupService
import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import DAL.Repository._
import DAL.Traits._

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module(environment: Environment,
             configuration: Configuration) extends AbstractModule {

  override def configure():Unit = {

    bind(classOf[IUserRepository]).to(classOf[UserRepository])
    bind(classOf[IUserDetailRepository]).to(classOf[UserDetailRepository])
    bind(classOf[IProjectRepository]).to(classOf[ProjectRepository])
    bind(classOf[ISkillRepository]).to(classOf[SkillRepository])
    bind(classOf[ILeaveRepository]).to(classOf[LeaveRepository])
    bind(classOf[ILeaveCategoryRepository]).to(classOf[LeaveCategoryRepository])

    bind(classOf[IUserSkillRepository]).to(classOf[UserSkillRepository])
    bind(classOf[IUserProjectRepository]).to(classOf[UserProjectRepository])
    bind(classOf[IProjectSkillRepository]).to(classOf[ProjectSkillRepository])
    bind(classOf[IRoleRepository]).to(classOf[RoleRepository])
    bind(classOf[IUserRoleRepository]).to(classOf[UserRoleRepository])

    bind(classOf[StartupService]).asEagerSingleton()
  }
}
