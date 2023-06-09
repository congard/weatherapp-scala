package pl.edu.agh.congard.weatherapp.backend

import upickle.default.ReadWriter

case class GeoPlaceDetails(
    lat: Double,
    lon: Double,
    displayName: String
) derives ReadWriter {
    override def toString: String =
        f"GeoPlaceDetails {lat=$lat, lon=$lon, displayName=$displayName}"

    override def canEqual(other: Any): Boolean = other.isInstanceOf[GeoPlaceDetails]

    override def equals(other: Any): Boolean = other match
        case that: GeoPlaceDetails =>
            that.canEqual(this) &&
                lat == that.lat &&
                lon == that.lon &&
                displayName == that.displayName
        case _ => false

    override def hashCode(): Int =
        val state = Seq(lat, lon, displayName)
        state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
}