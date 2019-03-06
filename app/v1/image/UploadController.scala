package v1.image

import java.util.UUID.randomUUID

import javax.inject.Inject
import play.api._
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._


class UploadController @Inject() (val controllerComponents: ControllerComponents) extends BaseController {

  val logger: Logger = Logger(this.getClass)

  def process = Action {implicit request =>

    val errorFunction = { form: Form[URLForm.Data] => BadRequest }

    val successFunction = { data: URLForm.Data =>
      val jobId = randomUUID().toString
      for (url <- data.url ) {
        // asynchronously download and upload to oss bucket
        val f: Future[Unit] = AuthClient.transfer(url)
        // logger if failed
        f.failed.foreach(exc => logger.error("An error occured during file download.", exc))
      }
      Ok(Json.toJson(Map("jobId" -> jobId)))
    }

    val formValidationResult = URLForm.form.bindFromRequest

    formValidationResult.fold(errorFunction, successFunction)
  }
}
