package toumi

import java.io.File
import java.io.FileOutputStream

trait Printer {
  def println(s: String): Unit

  def close(): Unit
}

object Printer {
  class StandardOutput() extends Printer {
    override def println(s: String): Unit = _root_.scala.Predef.println(s)
    override def close(): Unit = ()
  }

  class FileOutput(file: File) extends Printer {
    val outputStream = FileOutputStream(file)

    override def println(s: String): Unit = {
      outputStream.write(s"${s}\n".getBytes())
    }
    override def close(): Unit = {
      outputStream.close()
    }
  }
}
