package ru.vood.flink.configuration

import org.apache.flink.api.java.utils.ParameterTool
import org.slf4j.LoggerFactory

import java.util.Properties
import scala.collection.JavaConverters._
import scala.collection.convert.ImplicitConversions.`map AsScala`
import scala.io.Source
import scala.language.implicitConversions
import scala.util.{Failure, Success, Try}

object ConfigUtils {
  private val logger = LoggerFactory.getLogger(getClass)

  implicit def getPropsFromMap(props: Map[String, String]): Properties = {
    val properites = new Properties()
    props.foreach(q=> properites.put(q._1,q._2))

//    val java = props.asJava
//    properites.putAll(java)
    properites
  }

  def getStringFromResourceFile(fileName: String): String = {
    val bufferSource = Source.fromURL(getClass.getClassLoader.getResource(fileName))
    val result = bufferSource.mkString
    bufferSource.close()
    result
  }

  def getPropsFromResourcesFile(fileName: String): Try[Map[String, String]] = Try {
    if (fileName.endsWith(".properties")) {
      val props = ParameterTool.fromPropertiesFile(Thread.currentThread.getContextClassLoader.getResourceAsStream(fileName))
      props.getProperties.toMap.asInstanceOf[Map[String, String]]
    } /*else if (fileName.endsWith(".yml")){
/*
      val yaml = new Yaml();
      val data = yaml.load(inputStream)
      System.out.println(data)
*/
      ???
    }*/
    else throw new IllegalArgumentException(s"Unable to read file $fileName ")

  }

  def getPropsFromArgs(args: Array[String] = Array[String]()): Try[Map[String, String]] = Try {
    if (args == null || args.isEmpty) return Try(Map[String, String]())
    val props = ParameterTool.fromArgs(args)
    props.getProperties.toMap.asInstanceOf[Map[String, String]]
  }


  implicit def proppsFromOptionalFile(fileName: String): Map[String, String] =
    getPropsFromResourcesFile(fileName) match {
      case Success(value) => value
      case Failure(_) => Map()
    }

  implicit def getAllProps(args: Array[String] = Array[String](), fileNames: List[String] = List()): AllApplicationProperties = {
    val propsFile = fileNames
      .map {
        getPropsFromResourcesFile
      }
      .flatMap(_ match {
        case Success(value) => value
        case Failure(exception) => throw exception
      }).toMap

    AllApplicationProperties(
      propsFile ++
        getPropsFromArgs(args).get
    )
  }
}

