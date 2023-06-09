package pl.edu.agh.congard.weatherapp.backend.provider

import pl.edu.agh.congard.weatherapp.backend.GeoPlaceDetails
import pl.edu.agh.congard.weatherapp.backend.ext.ResponseExtensions
import requests.Response

import java.nio.charset.Charset
import java.util
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.CollectionConverters.*

/**
 * Uses OpenStreetMap to retrieve place's latitude / longitude
 * by its name, see
 * https://stackoverflow.com/questions/31811074/openstreetmap-get-coordinates-from-address
 */
class OSMReverseGeocodingProvider extends ReverseGeocodingProvider {
    override def getName: String = "OpenStreetMap"

    override def search(name: String): Future[Seq[GeoPlaceDetails]] = Future {
        val r: Response = requests.get("https://nominatim.openstreetmap.org/search",
            params = Map("q" -> name, "format" -> "json"))
        
        if !r.isSuccessful then
            Future.failed(IllegalStateException(s"Illegal status code: ${r.statusCode}"))

        val result = ListBuffer[GeoPlaceDetails]()
        val responseArray = r.asJSONArray

        for (place <- responseArray) {
            result += GeoPlaceDetails(
                place("lat").str.toDouble,
                place("lon").str.toDouble,
                place("display_name").str
            )
        }

        // constant time conversion
        result.toList
    }
}

object OSMReverseGeocodingProvider {
    private val provider = new OSMReverseGeocodingProvider()
    def apply(): OSMReverseGeocodingProvider = provider
}
