import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.diagrams.Diagrams

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, FileInputStream, PrintStream}

class BFSSpec extends AnyFlatSpec with Diagrams:
  "search" should "return all required kpoints" in {
    // searchの出力結果を収集
    val inputStream = new FileInputStream("src/test/resources/input.txt")
    val outputStream = new ByteArrayOutputStream
    System.setIn(inputStream)
    val (kpoints, xyRequiredValues, zRequiredValues) = Kpoint.readKpoints()
    val bfs = BFS(kpoints)
    Console.withOut(outputStream) {
      bfs.search(xyRequiredValues, zRequiredValues)
    }
    val out = outputStream.toString()

    // 出力結果が必要なkpointを全て含むかどうかを確認
    val testInputStream = new ByteArrayInputStream(out.getBytes)
    System.setIn(testInputStream)
    val (searchedKpoints, _, _) = Kpoint.readKpoints()
    val sortedXyRequiredValues = xyRequiredValues.toList.sorted
    val sortedZRequiredValues = zRequiredValues.toList.sorted
    val visited = Array.fill(xyRequiredValues.size, xyRequiredValues.size, zRequiredValues.size)(false)
    for kpoint <- searchedKpoints do
      val xIndex = sortedXyRequiredValues.indexOf(kpoint.x)
      val yIndex = sortedXyRequiredValues.indexOf(kpoint.y)
      val zIndex = sortedZRequiredValues.indexOf(kpoint.z)
      if xIndex != -1 && yIndex != -1 && zIndex != -1 then
        visited(xIndex)(yIndex)(zIndex) = true
    val total = xyRequiredValues.size * xyRequiredValues.size * zRequiredValues.size
    var cnt = 0
    for i <- 0 until xyRequiredValues.size do
      for j <- 0 until xyRequiredValues.size do
        for k <- 0 until zRequiredValues.size do
          if !visited(i)(j)(k) then
            println(s"(${sortedXyRequiredValues(i)}, ${sortedXyRequiredValues(j)}, ${sortedZRequiredValues(k)}) is not found")
            cnt += 1
    println(s"not found: $cnt/$total")
    assert(visited.flatten.flatten.forall(_ == true))
  }
