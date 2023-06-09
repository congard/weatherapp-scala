package pl.edu.agh.congard.weatherapp.backend.provider

import pl.edu.agh.congard.weatherapp.backend.GeoPlaceDetails

import scala.concurrent.Future

trait ReverseGeocodingProvider extends Nameable {
    def search(name: String): Future[Seq[GeoPlaceDetails]]

    override def toString: String =
        f"ReverseGeocodingProvider {name=$getName}"
}
