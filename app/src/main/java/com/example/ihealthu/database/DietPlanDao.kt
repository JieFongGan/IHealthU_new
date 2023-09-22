package com.example.ihealthu.database

//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.Query
//import androidx.room.Update
//
//@Dao
//interface DietPlanDao {
//    @Insert
//    fun insert(dietPlan: DietPlan)
//    @Update
//    fun update(dietPlan: DietPlan)
//    @Delete
//    fun delete(dietPlan: DietPlan)
//    @Query("SELECT * FROM DietPlan_table")
//    fun getAllPlans(): List<DietPlan>
//
//    @Query("SELECT * FROM DietPlan_table WHERE dp_Days = :day")
//    fun getPlansByDay(day: String): List<DietPlan>
//
//
//}