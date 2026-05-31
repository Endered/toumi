package toumi

import java.nio.file.Files
import java.nio.file.Path

def createTempFile(prefix: String, suffix: String): Path = {
  val p = Files.createTempFile(prefix, suffix)
  p.toFile().deleteOnExit()
  p
}
