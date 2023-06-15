package pl.edu.pja.prm_p1_s23711_mateusz_drabarek.repository

import com.google.android.gms.maps.model.LatLng

interface LocationRepository {
    var savedLocation: LatLng?
}