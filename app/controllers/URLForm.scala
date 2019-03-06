package controllers

object URLForm {
  import play.api.data.Forms._
  import play.api.data.Form


  case class Data(url: List[String])


  val form = Form(
    mapping(
      "url" -> list(nonEmptyText),
    )(Data.apply)(Data.unapply)
  )
}
