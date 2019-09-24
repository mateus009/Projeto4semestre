package com.example.streetsecuritynow

class resultados (val results:List<places>)

class places(val formatted_address:String ,val geometry:Geomatry, val name:String)

class Geomatry(val location:location)

class location(val lat:Double, val lng:Double)