package com.cpg12.findingfresh.database

import androidx.room.*

@Dao
interface FreshDAO {

    /**
    @Query("SELECT * FROM my_table")
    fun getAll(): LiveData<List<Entity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry : Entity)

    @Delete
    suspend fun deleteThis(entry : Entity?)
    **/
}