import toumi.Config
import mainargs.ParserForClass
import io.circe.parser.decode
import toumi.ClangExtractApiJson
import toumi.Filter
import toumi.withPrinter

object Main {
  def main(args: Array[String]): Unit = {
    val config = ParserForClass[Config].constructOrExit(args)
    val filter = Filter.fromRegexes(config.pattern)

    val expandedHeader = toumi.expandHeaders(config.clangSearchPath, config.headerPath)
    val extractedJson = toumi.clangExtract(expandedHeader)

    val parsed = decode[ClangExtractApiJson.Root](extractedJson).right.get

    val symbols = parsed.symbols.filter(x => filter.check(x.names.title))

    withPrinter(config) { printer =>
      symbols.foreach { symbol =>
        printer.println(symbol.declarationFragments.map(_.spelling).mkString(" "))
      }
    }
  }
}
