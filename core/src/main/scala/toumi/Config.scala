package toumi

import mainargs.main
import mainargs.arg
import mainargs.Flag

@main
case class Config(
    @arg(short = 'L', doc = "Search Paths for clang") clangSearchPath: Seq[String],
    @arg(short = 't', doc = "Header paths for search prototype definitions") headerPath: Seq[String],
    @arg(short = 'p', doc = "Regex patterns for extracting prototype definitions") pattern: Seq[String],
    @arg(short = 'o', doc = "File path to output result") output: Option[String],
)
