package controllers

import javax.inject.Inject
import models.Widget
import play.api.data._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.libs.json._
import play.api.Logger

/**
 * The classic WidgetController using MessagesAbstractController.
 *
 * Instead of MessagesAbstractController, you can use the I18nSupport trait,
 * which provides implicits that create a Messages instance from a request
 * using implicit conversion.
 *
 * See https://www.playframework.com/documentation/2.6.x/ScalaForms#passing-messagesprovider-to-form-helpers
 * for details.
 */


class WidgetController @Inject()(cc: MessagesControllerComponents) extends MessagesAbstractController(cc) {
  import WidgetForm._
  val logger: Logger = Logger(this.getClass)

  private val widgets = scala.collection.mutable.ArrayBuffer(
    Widget("Widget 1"),
    Widget("Widget 2"),
    Widget("Widget 3")
  )

  // The URL to the widget.  You can call this directly from the template, but it
  // can be more convenient to leave the template completely stateless i.e. all
  // of the "WidgetController" references are inside the .scala file.
  private val postUrl = routes.WidgetController.createWidget()

  def index = Action { implicit request: MessagesRequest[AnyContent] =>
    // Pass an unpopulated form to the template
    Ok(views.html.listWidgets(widgets, form, postUrl))
  }

  def listWidgets = Action { implicit request: MessagesRequest[AnyContent] =>
    // Pass an unpopulated form to the template
    Ok(views.html.listWidgets(widgets, form, postUrl))
  }

  // This will be the action that handles our form post
  def createWidget = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[Data] =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      BadRequest(views.html.listWidgets(widgets, formWithErrors, postUrl))
    }

    val successFunction = { data: Data =>
      // This is the good case, where the form was successfully parsed as a Data object.
      //val widget = Widget(url = data.url)
      //widgets.append(widget)
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
