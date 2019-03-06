package v1.image

import javax.inject.Inject

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

class UploadRouter @Inject() (controller: UploadController) extends SimpleRouter {
  val prefix = "/v1/image"

  override def routes: Routes = {
    case POST(p"/upload") => controller.process
  }
}
