package com.pmdk.gymapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pmdk.gymapp.data.dao.ExerciseDao
import com.pmdk.gymapp.data.dao.GymSessionDao
import com.pmdk.gymapp.data.dao.SessionExerciseDao
import com.pmdk.gymapp.data.dao.SetDao
import com.pmdk.gymapp.data.dao.UserDao
import com.pmdk.gymapp.data.entities.Exercise
import com.pmdk.gymapp.data.entities.GymSession
import com.pmdk.gymapp.data.entities.SessionExercise
import com.pmdk.gymapp.data.entities.Set
import com.pmdk.gymapp.data.entities.User
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
    version = 11,
    exportSchema = false
)
@TypeConverters(Converters::class)
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

        fun getInstance(context: Context): AppDatabase {
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
    ): Callback() {
        override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
            super.onCreate(db)
            // Populate the database with hardcoded exercises
            CoroutineScope(Dispatchers.IO).launch {
                val database = getInstance(context)
                val exerciseDao = database.exerciseDao()
                val userDao = database.userDao()

                val exercises = listOf(
                    Exercise(
                        name = "Bench",
                        descriptionEn = "A compound exercise targeting the chest, shoulders, and triceps by pressing a weight away from the chest while lying on a bench.",
                        descriptionEl = "Μια σύνθετη άσκηση που στοχεύει το στήθος, τους ώμους και τους τρικεφάλους πιέζοντας ένα βάρος μακριά από το στήθος ενώ ξαπλώνετε σε έναν πάγκο.",
                        icon = "bench",
                        muscleGroup = "Upper Body"
                    ),
                    Exercise(
                        name = "Biceps",
                        descriptionEn = "Isolation exercises that target the biceps muscles, such as curls using dumbbells, barbells, or cables.",
                        descriptionEl = "Ασκήσεις απομόνωσης που στοχεύουν στους δικέφαλους μύες, όπως κάμψεις με αλτήρες, μπάρες ή καλώδια.",
                        icon = "biceps",
                        muscleGroup = "Upper Body"
                    ),
                    Exercise(
                        name = "Cycling",
                        descriptionEn = "A cardio exercise that improves heart health and leg endurance, typically performed on a stationary bike or outdoors.",
                        descriptionEl = "Μια αερόβια άσκηση που βελτιώνει την υγεία της καρδιάς και την αντοχή των ποδιών, συνήθως με στατικό ποδήλατο ή σε εξωτερικούς χώρους.",
                        icon = "cycling",
                        muscleGroup = "Cardio"
                    ),
                    Exercise(
                        name = "Deadlift",
                        descriptionEn = "A full-body compound lift that primarily strengthens the lower back, hamstrings, and glutes by lifting a weight from the ground to hip level.",
                        descriptionEl = "Μια σύνθετη άρση για ολόκληρο το σώμα που ενισχύει κυρίως την κάτω πλάτη, τους οπίσθιους μηριαίους και τους γλουτούς, σηκώνοντας ένα βάρος από το έδαφος μέχρι το ύψος των γοφών.",
                        icon = "deadlift",
                        muscleGroup = "Lower Body"
                    ),
                    Exercise(
                        name = "Dips",
                        descriptionEn = "A bodyweight exercise that targets the chest, triceps, and shoulders by lowering and raising the body using parallel bars.",
                        descriptionEl = "Μια άσκηση με το βάρος του σώματος που στοχεύει στο στήθος, τους τρικεφάλους και τους ώμους, κατεβάζοντας και ανεβάζοντας το σώμα χρησιμοποιώντας παράλληλες μπάρες.",
                        icon = "dips",
                        muscleGroup = "Upper Body"
                    ),
                    Exercise(
                        name = "Pull-ups",
                        descriptionEn = "A compound upper body exercise that targets the lats, biceps, and shoulders by pulling the body up to a bar using a pronated grip.",
                        descriptionEl = "Μια σύνθετη άσκηση για το πάνω μέρος του σώματος που στοχεύει στους πλατείς ραχιαίους, τους δικέφαλους και τους ώμους, τραβώντας το σώμα προς τα πάνω σε μια μπάρα με ανάποδη λαβή.",
                        icon = "pullups",
                        muscleGroup = "Upper Body"
                    ),
                    Exercise(
                        name = "Romanian Deadlift",
                        descriptionEn = "A variation of the deadlift focusing on hamstring and glute engagement by lowering the weight with a slight bend in the knees.",
                        descriptionEl = "Μια παραλλαγή της άρσης θανάτου που εστιάζει στην ενεργοποίηση των οπίσθιων μηριαίων και των γλουτών, χαμηλώνοντας το βάρος με ελαφριά κάμψη στα γόνατα.",
                        icon = "rdl",
                        muscleGroup = "Lower Body"
                    ),
                    Exercise(
                        name = "Rows",
                        descriptionEn = "A pulling exercise that targets the back muscles, particularly the lats and rhomboids, performed with dumbbells, a barbell, or a cable machine.",
                        descriptionEl = "Μια άσκηση έλξης που στοχεύει στους μύες της πλάτης, ιδιαίτερα στους πλατείς ραχιαίους και στους ρομβοειδείς, που εκτελείται με αλτήρες, μπάρα ή μηχάνημα με καλώδια.",
                        icon = "rows",
                        muscleGroup = "Upper Body"
                    ),
                    Exercise(
                        name = "Snatch",
                        descriptionEn = "An advanced Olympic lift that involves explosively lifting a barbell from the ground to overhead in one motion, targeting the entire body.",
                        descriptionEl = "Μια προχωρημένη Ολυμπιακή άρση που περιλαμβάνει την εκρηκτική ανύψωση μιας μπάρας από το έδαφος πάνω από το κεφάλι με μία κίνηση, στοχεύοντας ολόκληρο το σώμα.",
                        icon = "snatch",
                        muscleGroup = "Lower Body"
                    ),
                    Exercise(
                        name = "Squat",
                        descriptionEn = "A foundational compound exercise that targets the quads, hamstrings, and glutes by lowering and raising the body with or without weights.",
                        descriptionEl = "Μια θεμελιώδης σύνθετη άσκηση που στοχεύει στους τετρακέφαλους, τους οπίσθιους μηριαίους και τους γλουτούς, κατεβάζοντας και ανεβάζοντας το σώμα με ή χωρίς βάρη.",
                        icon = "squat",
                        muscleGroup = "Lower Body"
                    ),
                    Exercise(
                        name = "Tricep Extensions",
                        descriptionEn = "An isolation exercise that targets the triceps, often performed using dumbbells, cables, or a barbell.",
                        descriptionEl = "Μια άσκηση απομόνωσης που στοχεύει στους τρικεφάλους, συχνά εκτελείται με αλτήρες, καλώδια ή μπάρα.",
                        icon = "triceps",
                        muscleGroup = "Upper Body"
                    ),
                    Exercise(
                        name = "Tricep Dips",
                        descriptionEn = "A bodyweight exercise that focuses on the triceps, performed using a bench or parallel bars to lower and raise the body.",
                        descriptionEl = "Μια άσκηση με το βάρος του σώματος που εστιάζει στους τρικεφάλους, εκτελείται χρησιμοποιώντας πάγκο ή παράλληλες μπάρες για να κατεβάσετε και να ανεβάσετε το σώμα.",
                        icon = "triceps2",
                        muscleGroup = "Upper Body"
                    )
                )

                exerciseDao.insertAll(exercises)
                // Insert Default Users
                val users = listOf(
                    User(
                        name = "Guest",
                        email = "guest@example.com",
                        passwordHash = "guest123",
                        age = null,
                        height = null,
                        weight = null,
                        profilePicture = null
                    ),
                )

                // Insert users into the database
                users.forEach { user ->
                    userDao.registerUser(user)
                }
            }
        }
    }
}