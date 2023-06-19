import scala.collection.mutable

class BFS:
  private val queued = mutable.Set[(Float, Float, Float)]()
  private val queue = mutable.Queue[Kpoint]()

  private def addKpoints(kpoints: List[Kpoint]): Unit =
    queue ++= kpoints
    for kpoint <- kpoints do
      queued += kpoint.position

  private def next(xyRequiredValues: mutable.Set[Float], zRequiredValues: mutable.Set[Float]) =
    val kpoint = queue.dequeue()
    if xyRequiredValues.contains(kpoint.position._1) && xyRequiredValues.contains(kpoint.position._2) && zRequiredValues.contains(kpoint.position._3) then
      kpoint.out()
    kpoint

  def search(xyRequiredValues: mutable.Set[Float], zRequiredValues: mutable.Set[Float]): Unit =
    while (queue.nonEmpty) {
      val kpoint = next(xyRequiredValues, zRequiredValues)
      val kpoints = kpoint.getSymmetricalPoint
      val newKpoints = kpoints.filter(kpoint => !queued.contains(kpoint.position))
      addKpoints(newKpoints)
    }

object BFS:
  def apply(kpoints: List[Kpoint]): BFS =
    val bfs = new BFS()
    bfs.addKpoints(kpoints)
    bfs