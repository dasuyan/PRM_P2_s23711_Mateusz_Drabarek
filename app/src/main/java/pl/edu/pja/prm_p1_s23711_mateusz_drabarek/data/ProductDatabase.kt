package pl.edu.pja.prm_p1_s23711_mateusz_drabarek.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.data.model.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1
)
abstract class ProductDatabase : RoomDatabase() {
    abstract val tasks: ProductDao

    companion object {
        fun open(context: Context): ProductDatabase = Room.databaseBuilder(
            context, ProductDatabase::class.java, "dishes.db"
        ).build()
    }
}