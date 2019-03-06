package v1.image

import java.net.URL
import com.aliyun.oss.OSSClient
import scala.concurrent._
import ExecutionContext.Implicits.global

object AuthClient {

  val endpoint = sys.env("OSS_ENDPOINT")
  val accessKeyId = sys.env("OSS_ACCESSKEYID")
  val accessKeySecret = sys.env("OSS_ACCESSKEYSECRET")
  val bucketName = sys.env("OSS_BUCKET")

  def transfer(url:String) = Future {
    val ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret)
    val inputStream = new URL(url).openStream
    val filename = scala.util.Random.alphanumeric.take(40).mkString("") + ".jpg"
    ossClient.putObject(bucketName, filename, inputStream)
    ossClient.shutdown
  }
}
