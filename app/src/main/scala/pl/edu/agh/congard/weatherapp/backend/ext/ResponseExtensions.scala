package pl.edu.agh.congard.weatherapp.backend.ext

// import org.json.{JSONArray, JSONObject}
import requests.Response

import java.nio.charset.Charset
import scala.collection.mutable.ArrayBuffer
import upickle.core.LinkedHashMap

implicit class ResponseExtensions(r: Response) {
    def asJSONObject: LinkedHashMap[String, ujson.Value] =
        ujson.read(r.asUTFString).obj
        
    def asJSONArray: ArrayBuffer[ujson.Value] =
        ujson.read(r.asUTFString).arr
        
    def asUTFString: String =
        String(r.data.array, Charset.forName("UTF-8"))
    
    def isSuccessful: Boolean =
        r.statusCode == 200
}
