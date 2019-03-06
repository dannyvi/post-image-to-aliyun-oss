package v1.image

import play.api.data.Form
import play.api.data.Forms.{list, mapping, nonEmptyText}

object URLForm {

  case class Data(url: List[String])

  val form = Form(
    mapping(
      "url" -> list(nonEmptyText),
    )(Data.apply)(Data.unapply)
  )

}