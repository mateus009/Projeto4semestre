package com.example.streetsecuritynow

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import okhttp3.*
import java.io.IOException
import com.google.gson.GsonBuilder
import android.R.string
import android.graphics.Color
import android.os.AsyncTask
import android.os.AsyncTask.execute
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.streetsecuritynow.HTTP.CADASTRO
import com.example.streetsecuritynow.HTTP.RequisicoesPostagem
import com.example.streetsecuritynow.ui.login.JsonParseRoutes
import feign.Feign
import feign.gson.GsonEncoder
import kotlinx.android.synthetic.main.activity_maps.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Array
import java.net.HttpURLConnection
import java.net.URL
import java.security.Key
import java.util.ArrayList


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?) = false

    var API = "a"

    var currentLatLng:LatLng = LatLng(0.0,0.0)
    private lateinit var mMap: GoogleMap
    private val placesClient: PlacesClient? = null
    val markerPoints:ArrayList<LatLng>? = ArrayList()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Initialize Places.
        Places.initialize(applicationContext, R.string.google_maps_key.toString())
// Create a new Places client instance.
        API = getString(R.string.google_maps_key)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)


        googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,R.raw.mapstyle));

        setUpMap()
        mMap.setOnMapLongClickListener(GoogleMap.OnMapLongClickListener{
              if (markerPoints != null && markerPoints!!.size > 1) {
                  markerPoints.clear();
                  mMap.clear();
              }
              println("ALTURAAAAAAAAAAAAA<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<"+it)
              println("ALTURAAAAAAAAAAAAA<<<<<<<<<<<<<<<<<<<<"+markerPoints)
              markerPoints!!.add(it);
              val options = MarkerOptions();
              options.position(it);

              if (markerPoints.size == 1) {
                  options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
              } else if (markerPoints.size == 2) {
                  options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
              }

              // Add new marker to the Google Map Android API V2
              mMap.addMarker(options);
              // Checks, whether start and end locations are captured

              if (markerPoints.size >= 2) {
                  val origin :LatLng = markerPoints.get(0)!!;
                  val dest : LatLng  = markerPoints.get(1)!!;
                  // Getting URL to the Google Directions API
                  val url : String = getDirectionsUrl(origin!!, dest!!);
                  val downloadTask = DownloadTask();
                  // Start downloading json data from Google Directions API
                  downloadTask.execute(url);
              }

        })

    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private inner class DownloadTask : AsyncTask<String,Void,String>() {

        @Override
        override fun doInBackground(vararg params: String ): String? {

            var data = "";

            try {
                data = downloadUrl(params[0]);
            } catch (e:Exception) {
                println("Background Task >>>>>>"+ e );
            }
            return data;
        }

         override fun onPostExecute (result:String ) {
            super.onPostExecute(result);
             var parserTask : ParserTask = ParserTask();
            parserTask.execute(result);
        }

    }

     inner class ParserTask : AsyncTask<String, Int, List<List<HashMap<String,String>>>>() {

        // Parsing the data in non-ui thread
        @Override
        override fun doInBackground(vararg params: String):List<List<HashMap<String,String>>>{

            var jObject:JSONObject;
            var routes : List<List<HashMap<String,String>>>? = null;
            try {
                jObject = JSONObject(params[0]);
                println(" ENTROU NO JSON FAZER ALGO NO JSONNNNNNNNNNNNN  >>>>>> " + jObject)
                println(" PARAMETROS >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + params[0] + "AGORA TODOS >>>>>>>>>> "+params)
                routes = JsonParseRoutes().parse(jObject);

                println(" ENTROU NO ROUTES >>>>>> " + routes)
            } catch (e:Exception) {
                println("ENTROU NO CATCHHHHHHHHHHHH >>>>>> " + e)
                e.printStackTrace();
            }
            println(" Retornando ROUTES >>>>>> " + routes)
            return routes!!
            //return routes!!;
        }


         override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
9        }
    }
    private fun downloadUrl(strUrl:String):String {
        var data:String = "";
        var iStream: InputStream? = null;
        var urlConnection: HttpURLConnection?  = null;
        try {
            val url:URL = URL(strUrl);

            url.openConnection();

            urlConnection = url.openConnection() as HttpURLConnection

            iStream = urlConnection.getInputStream();

            var br: BufferedReader  = BufferedReader(InputStreamReader(iStream));

            var sb:StringBuffer = StringBuffer();

            var line : String? = "";
            do{
                sb.append(line);
                line = br.readLine()
            }
            while (line != null)
            data = sb.toString();

            br.close();

        } catch (e:Exception) {
            println("Exception >>>>>>>>>>>>>>>>>" + e );
        } finally {
            iStream!!.close();
            urlConnection!!.disconnect();
        }
        println(data)
        return data;
    }
     private fun getDirectionsUrl(origin:LatLng , dest:LatLng ):String {

        // Origin of route
        val str_origin : String  = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        val str_dest : String = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        val sensor : String= "sensor=false";
        val mode:String  = "mode=walking";

        // Building the parameters to the web service
        val parameters:String = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        val output :String= "json";

        // Building the url to the web service
        val url :String= "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=${API}" ;
        println(url + " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> URL")

        return url;
    }
    private fun setUpMap() {

             if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                   ActivityCompat.requestPermissions(this,
                       arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
                   return
                }

        mMap.isMyLocationEnabled = true


        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.

            if (location != null) {
                lastLocation = location

                    currentLatLng = LatLng(location.latitude, location.longitude)
                    placeMarkerOnMap(currentLatLng)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))


            }
        }
    }
     fun PlacesHospital(View:View) {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true

        mMap.clear()
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.

        lastLocation = location

        fetchJson(location.latitude, location.longitude, "hospital")


        }
    }

    fun PlacesDelegMulher(View:View) {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true

        mMap.clear()
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.


            lastLocation = location

            fetchJson()


        }
    }

    fun PlacesPolice(View:View) {


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true

        mMap.clear()
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.


        lastLocation = location

        fetchJson(location.latitude, location.longitude, "police")


        }
    }

    private fun placeMarkerOnMap(location: LatLng) {

        val markerOptions = MarkerOptions().position(location)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(resources, R.mipmap.ic_user_location)))
        mMap.addMarker(markerOptions)

    }

    private fun fPlaceMarkerOnMap(location: LatLng) {

        val markerOptions = MarkerOptions().position(location)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(resources, R.mipmap.ic_femaleuser_location)))
        mMap.addMarker(markerOptions)

    }

    fun fetchJson(lat:Double, lng: Double, lcation:String ){
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=$lat,$lng&radius=15000&type=$lcation&key=$API"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()
                val resultado =  gson.fromJson(body, resultados::class.java)

                val currentLatLng = LatLng(resultado.results[1].geometry.location.lat,resultado.results[1].geometry.location.lng )
                println(resultado)
                println(body)

                var handler = Handler(Looper.getMainLooper());
                for(places in resultado.results){
                   val currentLatLng = LatLng(places.geometry.location.lat,places.geometry.location.lng )
                   println(places)
                    handler.post(Runnable {
                        placeMarkerOnMap(currentLatLng)
                    })
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
    @Override
    fun fetchJson(){
        val url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=delegacia%20da%20mulher&key=$API"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()
                val resultado =  gson.fromJson(body, resultados::class.java)
                println(resultado)
                val currentLatLng = LatLng(resultado.results[1].geometry.location.lat,resultado.results[1].geometry.location.lng )
                println(resultado)
                println(body)

                var handler = Handler(Looper.getMainLooper())
                for(places in resultado.results){
                    val currentLatLng = LatLng(places.geometry.location.lat,places.geometry.location.lng )
                    println(places)
                    handler.post(Runnable {
                        fPlaceMarkerOnMap(currentLatLng)

                    })
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }



}