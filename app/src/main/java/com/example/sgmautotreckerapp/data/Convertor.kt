package com.example.sgmautotreckerapp.data

import androidx.room.TypeConverter
import java.util.Date

class Convertor {

    // Конвертация Дейт
    // из стэмп в дейт
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    // из дейт в стэмп
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}