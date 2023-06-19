import java.util.Scanner
import scala.collection.mutable


class Kpoint(val x: Float, val y: Float, val z: Float, val energy: Float):
  private val transformValue = 1f

  def position: (Float, Float, Float) = (x, y, z)

  def getSymmetricalPoint: List[Kpoint] =
    List(
      useXTranslationSymmetry,
      useYTranslationSymmetry,
      use3foldRotationalSymmetry,
      use6foldRotationalSymmetryOnZ0
    ).flatten

  def out(): Unit =
    println(s"$x $y $z $energy")

  private def in_second_zone(x: Float): Boolean =
    x <= 1.5

  private def rot(x: Float, y: Float, angle: Float): (Float, Float) =
    val rad = Math.PI * angle
    val cos = Math.cos(angle).toFloat
    val sin = Math.sin(angle).toFloat
    (x * cos - y * sin, x * sin + y * cos)

  private def translateToFirstBrillouinZone: Kpoint =
    val tx = if x > 0.5 then -1 else 0
    val ty = if y > 0.5 then -1 else 0
    val translatedX = x + tx
    val translatedY = y + ty
    Kpoint(translatedX, translatedY, z, energy)

  private def useXTranslationSymmetry: Option[Kpoint] =
    val translatedX = x + transformValue
    if in_second_zone(translatedX) then
      Some(Kpoint(translatedX, y, z, energy))
    else
      None

  private def useYTranslationSymmetry: Option[Kpoint] =
    val translatedY = y + transformValue
    if in_second_zone(translatedY) then
      Some(Kpoint(x, translatedY, z, energy))
    else
      None

  private def use3foldRotationalSymmetry: Option[Kpoint] =
    val translated = translateToFirstBrillouinZone
    val (rotatedX, rotatedY) = rot(translated.x, translated.y, 1 / 3f)
    Some(Kpoint(rotatedX, rotatedY, translated.z, energy))

  private def use6foldRotationalSymmetryOnZ0: Option[Kpoint] =
    if z == 0 then
      Some(Kpoint(y, x, 0, energy))
    else
      None

object Kpoint:
  def apply(x: Float, y: Float, z: Float, energy: Float): Kpoint =
    new Kpoint(x, y, z, energy)

  def readKpoints(): (List[Kpoint], mutable.Set[Float], mutable.Set[Float]) =
    val sc = new Scanner(System.in)
    var kpoints = List[Kpoint]()
    val xyRequiredValues = mutable.Set[Float]()
    val zRequiredValues = mutable.Set[Float]()
    while sc.hasNextFloat do
      val x = sc.nextFloat()
      val y = sc.nextFloat()
      val z = sc.nextFloat()
      val energy = sc.nextFloat()
      val kpoint = new Kpoint(x, y, z, energy)
      kpoints = kpoint :: kpoints
      xyRequiredValues += x
      xyRequiredValues += y
      zRequiredValues += z
    (kpoints.reverse, xyRequiredValues, zRequiredValues)
