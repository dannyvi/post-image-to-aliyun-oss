package unit

import akka.stream.Materializer
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._
import v1.image.UploadController

import scala.concurrent.Future


class UnitSpec extends PlaySpec with GuiceOneAppPerSuite {

  implicit lazy val materializer: Materializer = app.materializer
  implicit lazy val Action = app.injector.instanceOf(classOf[DefaultActionBuilder])


  "UploadController#process" should {
    "should be valid" in {
      val controller = new UploadController(Helpers.stubControllerComponents())
      val request = FakeRequest("POST", "/v1/image/upload").withJsonBody(Json.parse(
        """{"url": ["https://imgstorev.oss-cn-beijing.aliyuncs.com/00003001ac2eabe0db2039ed650048de1609b5de.jpg", "https://imgstorev.oss-cn-beijing.aliyuncs.com/0002a56498c539e7360526a615ffb3147603b7de.png"]}"""))
      val apiResult: Future[Result] = call(controller.process, request)
      status(apiResult) mustEqual OK
      contentAsString(apiResult) must include("jobId")
    }
  }

  "An essential action" should {
    "can parse a JSON body" in {
      val action: EssentialAction = Action { request =>
        val value = (request.body.asJson.get \ "url").as[List[String]].mkString(" ")
        Ok(value)
      }
      val request = FakeRequest(POST, "/v1/image/upload").withJsonBody(Json.parse("""{"url": ["https://imgstorev.oss-cn-beijing.aliyuncs.com/00003001ac2eabe0db2039ed650048de1609b5de.jpg", "https://imgstorev.oss-cn-beijing.aliyuncs.com/0002a56498c539e7360526a615ffb3147603b7de.png"]}"""))
      val result = call(action, request)
      status(result) mustEqual OK
      contentAsString(result) mustEqual "https://imgstorev.oss-cn-beijing.aliyuncs.com/00003001ac2eabe0db2039ed650048de1609b5de.jpg https://imgstorev.oss-cn-beijing.aliyuncs.com/0002a56498c539e7360526a615ffb3147603b7de.png"
    }
  }

}

