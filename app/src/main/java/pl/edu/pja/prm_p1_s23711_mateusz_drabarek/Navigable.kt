package pl.edu.pja.prm_p1_s23711_mateusz_drabarek

import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.model.Product

interface Navigable {
    enum class Destination {
        List, Add, Photo, Map
    }
    fun navigate(to: Destination, product: Product?)
}