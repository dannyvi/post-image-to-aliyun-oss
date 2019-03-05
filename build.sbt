name := """post-image-to-aliyun-oss"""

version := "2.7.x"

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.11.12", "2.12.7")

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test
libraryDependencies ++= Seq(
    "com.aliyun.oss" % "aliyun-sdk-oss" % "2.8.3"
)
scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-Xfatal-warnings"
)
