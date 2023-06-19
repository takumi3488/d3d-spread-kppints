import java.util.Scanner
import java.io.{ByteArrayInputStream, FileInputStream, InputStream}

object Main:
  def main(args: Array[String]): Unit =
    System.setIn(new FileInputStream("src/test/resources/input.txt"))
    val (kpoints, xyRequiredValues, zRequiredValues) = Kpoint.readKpoints()
    val bfs = BFS(kpoints)
    bfs.search(xyRequiredValues, zRequiredValues)
