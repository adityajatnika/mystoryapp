package com.example.mystoryapp.ui.activity

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mystoryapp.R
import com.example.mystoryapp.data.local.pref.SessionManager
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.data.remote.response.StoryResponse
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import com.example.mystoryapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.DelicateCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var sessionManager: SessionManager
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var marker :  Marker

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, "masuk on create")
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val dicodingSpace = LatLng(-6.8957643, 107.6338462)

        Log.e(TAG, "masuk ke onready dulu")
        getUserLocation()
        getMyLocation()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dicodingSpace))

    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getUserLocation(){
        val token = sessionManager.fetchAuthToken()
        val client = ApiConfig.getApiService().getStoriesLocOn(
            token = StringBuilder("Bearer ").append(token).toString(),
            page = 1,
            loc = 1,
            size = 30
        )
        client.enqueue(object : Callback<StoryResponse<ListStoryItem>> {
            override fun onResponse(
                call: Call<StoryResponse<ListStoryItem>>,
                response: Response<StoryResponse<ListStoryItem>>
            ) {
                if (response.isSuccessful) {
                    if(response.body() != null){
                        for (item in response.body()!!.listStory){
                            marker = mMap.addMarker(
                                MarkerOptions()
                                    .position(LatLng(item.lat, item.lon))
                                    .title(item.name)
                                    .snippet(
                                        StringBuilder("created : ")
                                            .append(item.createdAt.subSequence(11, 16).toString())
                                            .toString()
                                    )
                            )!!
                        }
                    }
                }
            }

            override fun onFailure(call: Call<StoryResponse<ListStoryItem>>, t: Throwable) {
                Log.e(TAG, t.message.toString())
                t.printStackTrace()
            }
        })
    }

    // option menu map
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_map, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}