lazy val commonSettings = Seq(
  scalaVersion := "3.8.3",
)

lazy val root = project
  .in(file("."))
  .settings(
    commonSettings,
  )
  .aggregate(toumi, core)

lazy val linkingOptions = if (sys.env.contains("TOUMI_STATIC_LINK")) {
  Seq("-static")
} else {
  Seq()
}

lazy val toumi = project
  .in(file("toumi"))
  .enablePlugins(ScalaNativePlugin)
  .settings(
    commonSettings,
    nativeConfig ~= {
      _.withLinkingOptions(linkingOptions)
    },
  )
  .dependsOn(core)

lazy val core = project
  .in(file("core"))
  .enablePlugins(ScalaNativePlugin)
  .settings(
    commonSettings,
    libraryDependencies += "com.lihaoyi" %%% "mainargs" % "0.7.8",
    libraryDependencies += "io.circe" %%% "circe-parser" % "0.14.15",
  )
