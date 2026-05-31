package toumi

import scala.util.matching.compat.Regex

sealed trait Filter {
  def check(name: String): Boolean
}

object Filter {
  case class RegexFilter(reg: Regex) extends Filter {
    override def check(name: String): Boolean = reg.matches(name)
  }

  case class OrFilter(filters: Seq[Filter]) extends Filter {
    override def check(name: String): Boolean = filters.exists(_.check(name))
  }

  def fromRegex(reg: String): RegexFilter = RegexFilter(Regex(reg))
  def fromRegexes(regs: Seq[String]): OrFilter = OrFilter(regs.map(fromRegex(_)))
}
