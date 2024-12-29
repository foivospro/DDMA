package com.example.gymappdemo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gymappdemo.data.dao.ExerciseDao
import com.example.gymappdemo.data.dao.GymSessionDao
import com.example.gymappdemo.data.dao.SessionExerciseDao
import com.example.gymappdemo.data.dao.SetDao
import com.example.gymappdemo.data.dao.UserDao
import com.example.gymappdemo.data.entities.Exercise
import com.example.gymappdemo.data.entities.GymSession
import com.example.gymappdemo.data.entities.SessionExercise
import com.example.gymappdemo.data.entities.Set
import com.example.gymappdemo.data.entities.User

@Database(
    entities = [
        User::class,
        GymSession::class,
        Exercise::class,
        SessionExercise::class,
        Set::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs
    abstract fun userDao(): UserDao
    abstract fun gymSessionDao(): GymSessionDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun sessionExerciseDao(): SessionExerciseDao
    abstract fun setDao(): SetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gym_database"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration() // Use this cautiously. Replace with migrations if needed.
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
            super.onCreate(db)
            // Optional: Add pre-population logic here if needed
        }
    }
}