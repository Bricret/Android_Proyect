package com.example.agenda.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.agenda.dao.PersonalDao
import com.example.agenda.models.Personal


@Database(
    entities = [Personal::class],
    version = 1
)
abstract class PersonalDb:RoomDatabase() {
    abstract fun personalDao():PersonalDao
}