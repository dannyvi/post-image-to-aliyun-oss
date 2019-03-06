package v1.image

import javax.inject.Inject
import play.api._

import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent._

import play.api.data.Forms._
import play.api.data.Form


object URLForm {

  case class Data(url: List[String])

  val form = Form(
    mapping(
      "url" -> list(nonEmptyText),
    )(Data.apply)(Data.unapply)
  )
}

class UploadController @Inject() (val controllerComponents: ControllerComponents) extends BaseController {

  val logger: Logger = Logger(this.getClass)

  def process = Action {implicit request =>

    val errorFunction = { formWithErrors: Form[URLForm.Data] =>
      // This is the bad case, where the form had validation errors.
      // Note how we pass the form with errors to the template.
      BadRequest
    }

    val successFunction = { data: URLForm.Data =>
      import java.net.URL
      import java.util.UUID.randomUUID

      val jobId = randomUUID().toString

      import com.aliyun.oss.OSSClient


      val endpoint = sys.env("OSS_ENDPOINT")
      val accessKeyId = sys.env("OSS_ACCESSKEYID")
      val accessKeySecret = sys.env("OSS_ACCESSKEYSECRET")
      val bucketName = sys.env("OSS_BUCKET")

      import scala.concurrent._
      import scala.util.{Success, Failure}
      import ExecutionContext.Implicits.global


      for (url <- data.url ) {
        val f = Future {
          val ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret)
          val inputStream = new URL(url).openStream
          val filename = scala.util.Random.alphanumeric.take(40).mkString("") + ".jpg"
          ossClient.putObject(bucketName, filename, inputStream)
          ossClient.shutdown
        }
        f.failed.foreach(exc => logger.error("An error occured during file download.", exc))
      }

      Ok(Json.toJson(
        Map("jobId" -> jobId)
      ))
    }

    val formValidationResult = URLForm.form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)

  }
}
