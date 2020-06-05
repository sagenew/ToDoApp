package com.example.todoapp.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class ToDoTaskDatabase : RoomDatabase() {

    abstract val toDoTaskDao : ToDoTaskDao

    companion object {
        @Volatile
        private var INSTANCE: ToDoTaskDatabase? = null

        fun getInstance(context: Context): ToDoTaskDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ToDoTaskDatabase::class.java,
                        "to_do_tasks_database"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}