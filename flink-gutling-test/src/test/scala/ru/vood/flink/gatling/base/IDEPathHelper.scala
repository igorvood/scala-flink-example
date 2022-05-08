package ru.vood.flink.gatling.base

import java.nio.file.{Path, Paths}

object IDEPathHelper {

  //  val mavenSourcesDirectory: Path = mavenSrcTestDirectory.resolve("scala")
  lazy val mavenResourcesDirectory: Path = mavenSrcTestDirectory.resolve("resources")
  lazy val mavenBinariesDirectory: Path = mavenTargetDirectory.resolve("test-classes")
  lazy val resultsDirectory: Path = mavenTargetDirectory.resolve("gatlingReport")
  private lazy val projectRootDir = Paths.get(getClass.getClassLoader.getResource("gatling.conf").toURI).getParent.getParent.getParent
  private lazy val mavenTargetDirectory = projectRootDir.resolve("target")
  private lazy val mavenSrcTestDirectory = projectRootDir.resolve("src").resolve("test")
  //  val recorderConfigFile: Path = mavenResourcesDirectory.resolve("recorder.conf")
}
