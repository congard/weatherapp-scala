package pl.edu.agh.congard.weatherapp.backend.provider

import pl.edu.agh.congard.weatherapp.backend.GeoPlaceDetails
import pl.edu.agh.congard.weatherapp.backend.ext.ScopeFunExt
import upickle.default as upickle

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import scala.collection.mutable.ListBuffer
import scala.io.{Codec, Source}
import scala.util.Using

object SettingsProvider {
    private val file = File("settings.json")
    private val _places = ListBuffer[GeoPlaceDetails]()

    load()

    private def load(): Unit = if (file.exists()) {
        Using(Source.fromFile(file)(Codec.UTF8)) { src =>
            val settingsStr = src.getLines().mkString
            ujson.read(settingsStr)("places").also { placesJa =>
                _places.addAll(upickle.read[List[GeoPlaceDetails]](placesJa))
            }
        }
    }

    private def save(): Unit = {
        val settings = ujson.Obj("places" -> upickle.writeJs(_places))
        Files.write(file.toPath, settings.toString.getBytes(StandardCharsets.UTF_8))
    }

    def addPlace(place: GeoPlaceDetails): Unit = {
        _places += place
        save()
    }

    def removePlace(place: GeoPlaceDetails): Unit = {
        _places -= place
        save()
    }

    def places: Seq[GeoPlaceDetails] = _places.toList
}
