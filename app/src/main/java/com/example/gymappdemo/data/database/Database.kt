package com.example.gymappdemo.data.database

import android.content.Context
import android.util.Log
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
import com.example.gymappdemo.data.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Database(
    entities = [
        User::class,
        GymSession::class,
        Exercise::class,
        SessionExercise::class,
        Set::class
    ],
    version = 5,
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
                    Exercise(
                        name = "Bench",
                        description = "A compound exercise targeting the chest, shoulders, and triceps by pressing a weight away from the chest while lying on a bench.",
                        icon = "bench",
                        muscleGroup = "Upper Body"
                    ),
                    Exercise(
                        name = "Biceps",
                        description = "Isolation exercises that target the biceps muscles, such as curls using dumbbells, barbells, or cables.",
                        icon = "biceps",
                        muscleGroup = "Upper Body"
                    ),
                    Exercise(
                        name = "Cycling",
                        description = "A cardio exercise that improves heart health and leg endurance, typically performed on a stationary bike or outdoors.",
                        icon = "cycling",
                        muscleGroup = "Cardio"
                    ),
                    Exercise(
                        name = "Deadlift",
                        description = "A full-body compound lift that primarily strengthens the lower back, hamstrings, and glutes by lifting a weight from the ground to hip level.",
                        icon = "deadlift",
                        muscleGroup = "Lower Body"
                    ),
                    Exercise(
                        name = "Dips",
                        description = "A bodyweight exercise that targets the chest, triceps, and shoulders by lowering and raising the body using parallel bars.",
                        icon = "dips",
                        muscleGroup = "Upper Body"
                    ),
                    Exercise(
                        name = "Pull-ups",
                        description = "A compound upper body exercise that targets the lats, biceps, and shoulders by pulling the body up to a bar using a pronated grip.",
                        icon = "pullups",
                        muscleGroup = "Upper Body"
                    ),
                    Exercise(
                        name = "Romanian Deadlift",
                        description = "A variation of the deadlift focusing on hamstring and glute engagement by lowering the weight with a slight bend in the knees.",
                        icon = "rdl",
                        muscleGroup = "Lower Body"
                    ),
                    Exercise(
                        name = "Rows",
                        description = "A pulling exercise that targets the back muscles, particularly the lats and rhomboids, performed with dumbbells, a barbell, or a cable machine.",
                        icon = "rows",
                        muscleGroup = "Upper Body"
                    ),
                    Exercise(
                        name = "Snatch",
                        description = "An advanced Olympic lift that involves explosively lifting a barbell from the ground to overhead in one motion, targeting the entire body.",
                        icon = "snatch",
                        muscleGroup = "Lower Body"
                    ),
                    Exercise(
                        name = "Squat",
                        description = "A foundational compound exercise that targets the quads, hamstrings, and glutes by lowering and raising the body with or without weights.",
                        icon = "squat",
                        muscleGroup = "Lower Body"
                    ),
                    Exercise(
                        name = "Tricep Extensions",
                        description = "An isolation exercise that targets the triceps, often performed using dumbbells, cables, or a barbell.",
                        icon = "triceps",
                        muscleGroup = "Upper Body"
                    ),
                    Exercise(
                        name = "Tricep Dips",
                        description = "A bodyweight exercise that focuses on the triceps, performed using a bench or parallel bars to lower and raise the body.",
                        icon = "triceps2",
                        muscleGroup = "Upper Body"
                    )
                )
                exerciseDao.insertAll(exercises)
                val insertedExercises = exerciseDao.getAllExercises()
                Log.d("Debug", "Inserted exercises: $insertedExercises")
                val user = User(
                    id = 1,
                    name = "John Doe",
                    email = "john.doe@example.com",
                    passwordHash = "hashed_password"
                )
                UserRepository.insertUser(user)
                Log.d("Debug", "User inserted: $user")
            }
        }
    }
}