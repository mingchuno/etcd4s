addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.18")

libraryDependencies ++= Seq(
  "com.github.os72" % "protoc-jar" % "3.6.0",
  "com.thesamet.scalapb" %% "compilerplugin" % "0.8.0"
)
