//package com.example.ihealthu.database
//
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//
//
//@Entity(tableName = "DietPlan_table")
//
//data class DietPlan (
//    @PrimaryKey(autoGenerate = true)
//    var dpID: Long = 0L,
//    @ColumnInfo(name = "dp_PlanName")
//    val dpPlanName: String,
//
//    @ColumnInfo(name = "dp_AgeRange")
//    var dpAgeRange: String,
//
//    @ColumnInfo(name = "dp_Purpose")
//    var dpPurpose: String,
//
//    @ColumnInfo(name = "dp_MonBFTime")
//    var dpMonBFTime: String,
//    @ColumnInfo(name = "dp_TueBFTime")
//    var dpTueBFTime: String,
//    @ColumnInfo(name = "dp_WedBFTime")
//    var dpWedBFTime: String,
//)