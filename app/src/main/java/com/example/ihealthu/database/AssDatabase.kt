package com.example.ihealthu.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 3, exportSchema = false)
abstract class AssDatabase : RoomDatabase() {
    abstract val assDatabaseDao: AssDatabaseDao
    companion object{
        @Volatile
        private var INSTANCE: AssDatabase? = null

        fun getInstance(context: Context): AssDatabase{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AssDatabase::class.java,
                        "AssDatabase"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    } //ensure one access into database
}
