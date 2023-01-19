package com.example.cardapp.datasource.local.location

import android.Manifest
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val INTERVAL = 300_000L

@RequiresPermission(
    anyOf = [
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION]
)
fun FusedLocationProviderClient.toLocationFlow(
    interval: Long = INTERVAL,
): Flow<Location> = callbackFlow {
    val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            for (location in result.locations)
                trySend(location)
        }
    }
    val locationRequest = LocationRequest.Builder(interval)
        .setPriority(PRIORITY_HIGH_ACCURACY)
        .build()
    requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())
        .addOnFailureListener { e -> close(e) }
    awaitClose { removeLocationUpdates(callback) }
}

@RequiresPermission(
    anyOf = [
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION]
)
suspend fun FusedLocationProviderClient.provideLastLocation() = suspendCoroutine<Location?> {
    lastLocation.addOnSuccessListener { location ->
        it.resume(location)
    }
}
