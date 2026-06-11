import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

lazy val rootPath = Paths.get(".")

lazy val dist = taskKey[Path]("Build and upload to ./dist directory")

lazy val commonSettings = Seq(
  scalaVersion := "3.8.3",
)

lazy val root = project
  .in(file("."))
  .settings(
    commonSettings,
    // LICENSES
    licenseReportTitle := "toumi-licenses",
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
    dist := {
      val binaryFile = (Compile / nativeLinkReleaseFull).value
      val binaryName = name.value
      val uploadTo = rootPath / "dist" / binaryName
      println(s"${binaryFile.toPath()} ${uploadTo}")
      if (!Files.exists(uploadTo.getParent())) {
        Files.createDirectories(uploadTo.getParent())
      }
      Files.copy(binaryFile.toPath(), uploadTo, StandardCopyOption.REPLACE_EXISTING)
      uploadTo
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
