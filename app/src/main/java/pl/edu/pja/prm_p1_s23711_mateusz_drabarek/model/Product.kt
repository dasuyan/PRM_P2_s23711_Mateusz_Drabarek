package pl.edu.pja.prm_p1_s23711_mateusz_drabarek.model

import android.net.Uri

data class Product(
    val id: Long,
    var name: String,
    var description: String,
    var resId: Uri
)
