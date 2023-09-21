//package com.example.ihealthu.database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//@Database(entities = [DietPlan::class], version = 1, exportSchema = false)
//abstract class IHUdatabase : RoomDatabase() {
//    abstract val dietDatabaseDao: DietDatabaseDao
//    companion object {
//        @Volatile
//        private var INSTANCE: IHUdatabase? = null
//
//        fun getInstance(context: Context): IHUdatabase {
//            synchronized(this) {
//                var instance = INSTANCE
//
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        IHUdatabase::class.java,
//                        "IHU_database"
//                    )
//                        .fallbackToDestructiveMigration()
//                        .build()
//                    INSTANCE = instance
//                }
//                return instance
//            }
//        }
//    } //ensure one access into database
//}