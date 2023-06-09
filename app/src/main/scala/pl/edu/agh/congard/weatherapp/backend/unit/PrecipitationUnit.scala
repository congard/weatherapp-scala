package pl.edu.agh.congard.weatherapp.backend.unit

enum PrecipitationUnit(p: Double, name: String) extends MeasureUnit(p, name) {
    case Millimeters(mm: Double) extends PrecipitationUnit(mm, "mm")
    case Centimeters(cm: Double) extends PrecipitationUnit(cm, "cm")

    def toMillimeters: Millimeters = this match
        case Millimeters(mm) => asInstanceOf[Millimeters]
        case Centimeters(cm) => Millimeters(cm * 10)
}

object PrecipitationUnit {
    private final val millimetersStr = "mm"
    private final val centimetersStr = "cm"
    
    def byName(value: Double, name: String): PrecipitationUnit = name match
        case PrecipitationUnit.millimetersStr => PrecipitationUnit.Millimeters(value)
        case PrecipitationUnit.centimetersStr => PrecipitationUnit.Centimeters(value)
        case _ => throw IllegalArgumentException(s"Unknown precipitation unit: $name")
}
