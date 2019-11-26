package com.example.streetsecuritynow.ui.login

import com.google.android.gms.maps.model.LatLng

data class JsonParseMateus (val geocoded_waypoints:List<Status?>?,val routes:List<rotas?>?,val status:String?)

data class Status(val geocoder_status:String?,val place_id:String?,val types:List<String?>?)

data class rotas(val Bounds:bounds?,val copyrights:String?,val legs:List<Legs?>?,
                 val via_waypoint:List<Any?>,val overview_polyline:Points?
                 ,val summary:String,val Warnings:List<String?>,val waypoint_order:List<Any?>)

data class Points(val points:String?)

data class bounds(val northeast:LatLng?,val southwest:LatLng?)

data class Legs(val distance:distancia?,val duration:distancia?,val end_address:String?, val end_location:LatLng?,
                val start_address:String?,val start_location:LatLng?,val steps:List<Steps?>?,val overview_polyline:String?
                ,val summary:String,val warnings:List<String?>,
                val waypoint_order:List<Any?>,val traffic_speed_entry: List<Any?>,val via_waypoint:List<Any?>)

data class Steps(val distance:distancia?,val duration:distancia?,val end_location:LatLng?,
                 val html_instructions:String?,val maneuver:String?,
                 val polyline:Polyline?,val start_location:LatLng?,val travel_mode:String?)

data class Polyline(val points:String?)
data class distancia(val text:String?,val value:Int?)
