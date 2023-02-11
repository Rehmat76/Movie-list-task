package com.shoppingfoodcart.firstassigment.utils.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shoppingfoodcart.firstassigment.models.generalModels.Results


@Database(
    entities = arrayOf( Results::class),
    version = 1,
    exportSchema = false
)
abstract class MyRoomDataBase : RoomDatabase() {

    abstract fun moviesListDao(): MoviesListDao

    companion object {
        @Volatile
        private var INSTANCE: MyRoomDataBase? = null

        fun getDatabase(context: Context): MyRoomDataBase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyRoomDataBase::class.java,
                    "movies_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
