import scala.sys.process.Process
import java.nio.file.Files
import scala.io.Source
import java.nio.file.Paths

package object toumi {
  def expandHeaders(searchPaths: Seq[String], headerPaths: Seq[String]): String = {
    val searchPathArgs = ("." +: searchPaths).map(s => s"-I${s}")

    val headerFile = createTempFile("toumi-header", ".h")
    val headerFileContents = headerPaths.map(p => s"""#include"${p}" """).mkString("\n")
    Files.write(headerFile, headerFileContents.getBytes())

    Process("clang", searchPathArgs ++ Seq("-P", "-E", headerFile.toAbsolutePath().toString())).!!
  }

  def clangExtract(content: String): String = {
    val inputFile = createTempFile("toumi-extract-input", ".h")
    Files.write(inputFile, content.getBytes())

    val outputFile = createTempFile("toumi-extract-output", ".json")

    Process("clang", Seq("-extract-api", "-x", "c-header", "-o", outputFile.toAbsolutePath().toString(), inputFile.toAbsolutePath().toString())).!!

    Source.fromFile(outputFile.toFile()).getLines().mkString("\n")
  }

  def symbolToOutput(symbol: toumi.ClangExtractApiJson.Symbol, outputOnlyName: Boolean): String = {
    outputOnlyName match {
      case true  => symbol.names.title
      case false => symbol.declarationFragments.map(_.spelling).mkString(" ")
    }
  }

  def withPrinter[T](config: Config)(f: Printer => T): T = {
    val printer = config.output match {
      case Some(f) =>
        val p = Paths.get(f)
        Files.deleteIfExists(p)
        Files.createFile(p)
        new Printer.FileOutput(p.toFile())
      case None =>
        new Printer.StandardOutput()
    }

    val res = f(printer)
    printer.close()

    res
  }
}
