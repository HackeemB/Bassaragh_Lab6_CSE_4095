package com.example.lab6_04_04_2022.model
import kotlin.collections.MutableList

var stations: MutableList<RadioStation> = mutableListOf(RadioStation())


class RadioStations() {

    init {
        stations.add(RadioStation("Star Radio Show 95.7", "https://sonicpanel.us/8020/stream/;"))
        stations.add(RadioStation("Aardvark Blues FM", "http://streaming.live365.com/b77280_128mp3"))
        stations.add(RadioStation("Houston Blues Radio", "http://streaming.live365.com/b76353_128mp3"))
        stations.add(RadioStation("Krush Yaad Jamaica", "https://s3.voscast.com:8469/stream"))
        stations.add(RadioStation("Radio France Inter", "http://direct.franceinter.fr/live/franceinter-midfi.mp3"))
        stations.add(RadioStation("Meta Candela - 93.1 FM", "http://streaming.live365.com/a81215"))
        stations.add(RadioStation("Radio Toca a Dancar", "https://sp0.redeaudio.com/9974/stream/"))
        stations.add(RadioStation("Zip 103 FM", "http://stream.zeno.fm/30nm88cf2v8uv"))
        stations.add(RadioStation("Hit Radio - 100% Mgharba", "http://mgharba.ice.infomaniak.ch/mgharba-128.mp3"))
        stations.add(RadioStation("Stereo Anime", "http://evcast.mediacp.eu:1750/;;"))

    }

    public fun getStations() : MutableList<RadioStation> {

        return stations
    }

    public fun size() : Int {
        return stations.size
    }
}