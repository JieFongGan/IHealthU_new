//package com.example.ihealthu.database
//
//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
//import androidx.room.Update
//
//@Dao
//interface DietDatabaseDao {
//    @Insert
//    suspend fun insert(diet: DietPlan)
//    @Update
//    suspend fun update(diet: DietPlan)
//
//    @Query("SELECT * from DietPlan_table WHERE dpID = :key")
//    suspend fun get(key: Long): DietPlan?
//
//    @Query("DELETE FROM DietPlan_table")
//    suspend fun clear()
//
//    @Query("SELECT * FROM DietPlan_table ORDER BY dpID ASC")
//    fun getAllDietPlanData(): LiveData<List<DietPlan>>
//}