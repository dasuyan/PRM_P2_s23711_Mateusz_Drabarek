package pl.edu.pja.prm_p1_s23711_mateusz_drabarek.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import pl.edu.pja.prm_p1_s23711_mateusz_drabarek.data.model.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM task;")
    suspend fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM task WHERE id = :id;")
    suspend fun getTask(id: Long): ProductEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(newTask: ProductEntity)

    @Update
    suspend fun updateTask(newTask: ProductEntity)

    @Query("DELETE FROM task WHERE id = :id;")
    suspend fun removeTask(id: Long)
}