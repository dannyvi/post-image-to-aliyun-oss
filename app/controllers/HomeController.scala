package controllers

import javax.inject.Inject
import play.api.data._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.libs.json._
import play.api.Logger

import v1.image

/**
 * The classic HomeController using MessagesAbstractController.
 *
 * Instead of MessagesAbstractController, you can use the I18nSupport trait,
 * which provides implicits that create a Messages instance from a request
 * using implicit conversion.
 *
 * See https://www.playframework.com/documentation/2.6.x/ScalaForms#passing-messagesprovider-to-form-helpers
 * for details.
 */


class HomeController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  import URLForm._
  val logger: Logger = Logger(this.getClass)

  // The URL to the widget.  You can call this directly from the template, but it
  // can be more convenient to leave the template completely stateless i.e. all
  // of the "HomeController" references are inside the .scala file.
  private val postUrl = routes.HomeController.createWidget()

  //val pUrl = v1.image.UploadRouter.routes

  def index = Action { implicit request: MessagesRequest[AnyContent] =>
    // Pass an unpopulated form to the template
    Ok(views.html.inputURL(form, postUrl))
  }

  // This will be the action that handles our form post
  def createWidget = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[Data] =>
      // This is the bad case, where the form had validation errors.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.inputURL(formWithErrors, postUrl))
    }

    val successFunction = { data: Data =>
      import java.net.URL
      import java.util.UUID.randomUUID

      val jobId = randomUUID().toString
      println(data.url)
      println(jobId)

      import com.aliyun.oss.OSSClient
      import java.io.InputStream


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

    val formValidationResult = form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }
}
