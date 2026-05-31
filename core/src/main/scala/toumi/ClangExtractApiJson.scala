package toumi

import io.circe.Decoder
import io.circe.Encoder
import io.circe.Json
import io.circe.HCursor
import io.circe.Decoder.Result

object ClangExtractApiJson {
  case class Root(
      symbols: Seq[Symbol],
  )

  case class Symbol(
      declarationFragments: Seq[DeclarationFragment],
      kind: Kind,
      names: Names,
  )
  case class DeclarationFragment(
      kind: String,
      spelling: String,
  )

  case class Kind(
      displayName: String,
  )

  case class Names(
      title: String,
  )

  given Decoder[Root] = Decoder.instance[Root] { c =>
    for {
      symbols <- c.get[Seq[Symbol]]("symbols")
    } yield Root(symbols)

  }

  given Decoder[Symbol] = Decoder.instance[Symbol] { c =>
    for {
      declarationFragments <- c.get[Seq[DeclarationFragment]]("declarationFragments")
      kind <- c.get[Kind]("kind")
      names <- c.get[Names]("names")
    } yield Symbol(declarationFragments, kind, names)
  }

  given Decoder[DeclarationFragment] = Decoder.instance[DeclarationFragment] { c =>
    for {
      kind <- c.get[String]("kind")
      spelling <- c.get[String]("spelling")
    } yield DeclarationFragment(kind, spelling)
  }

  given Decoder[Kind] = Decoder.instance[Kind] { c =>
    for {
      displayName <- c.get[String]("displayName")
    } yield Kind(displayName)
  }

  given Decoder[Names] = Decoder.instance[Names] { c =>
    for {
      title <- c.get[String]("title")
    } yield Names(title)

  }
}
