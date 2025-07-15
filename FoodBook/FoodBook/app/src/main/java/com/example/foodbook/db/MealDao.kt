package com.example.foodbook.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.foodbook.pojo.Meal

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal:Meal)

    @Update
    suspend fun updateMeal(meal:Meal)

    @Delete
    suspend fun deleteMeal(meal:Meal)

    @Query("SELECT * FROM mealInformation")
    fun getAllMeals():LiveData<List<Meal>> // functions returning LiveData are not marked as suspend because LiveData handles threading internally

}
/*
Asynchronous Operation: The suspend keyword indicates
that the function is a suspending function. It means this
function must be called from within a coroutine or another suspending function.
The operation is performed on a background thread, ensuring that it doesn't block
the main thread. This is crucial for operations that might take some time,
like inserting, updating, or deleting data in a database.

 */