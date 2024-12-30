package com.example.gymappdemo.data.database

import android.content.Context
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    .addCallback(PrepopulateCallback(context))
                    .fallbackToDestructiveMigration() // Use this cautiously. Replace with migrations if needed.
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class PrepopulateCallback(
        private val context: Context
    ): RoomDatabase.Callback() {
        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
            super.onCreate(db)
            // Populate the database with hardcoded exercises
            CoroutineScope(Dispatchers.IO).launch {
                val database = getInstance(context)
                val exerciseDao = database.exerciseDao()

                val exercises = listOf(
                    Exercise(name = "Push-up", description = "Upper body exercise.", icon = "weightlifter", muscleGroup = "Chest"),
                    Exercise(name = "Squat", description = "Lower body exercise.", icon = "weightlifter", muscleGroup = "Legs"),
                    Exercise(name = "Plank", description = "Core stability exercise.", icon = "weightlifter", muscleGroup = "Core")
                )

                exerciseDao.insertAll(exercises)
            }
        }
    }
}