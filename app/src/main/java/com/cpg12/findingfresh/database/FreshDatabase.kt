package com.cpg12.findingfresh.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * @Database(entities = [User::class], version = 1)
abstract class ProjectDatabase : RoomDatabase() {
abstract fun projectDao(): ProjectDao
}
 */

@Database(entities = [FreshEntity::class], version = 2)
abstract class FreshDatabase : RoomDatabase() {

    abstract fun projectDao() : FreshDAO

    companion object {

        @Volatile
        private var INSTANCE: FreshDatabase? = null

        fun getInstance(context: Context): FreshDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FreshDatabase::class.java, "project-db"
            ).fallbackToDestructiveMigration()
                .build()
    }
}