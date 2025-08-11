package com.shoppingplanning.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StringUtil {
    companion object {

        fun getYear(): Int {

            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.YEAR)
        }


        fun getMonth(): Int {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.MONTH) + 1
        }

        fun getShortDate(): String {

            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = getShortMonthName(calendar.get(Calendar.MONTH) + 1)
            return "$day $month"
        }

        fun getNameMonth(month: Int): String {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MONTH, month - 1)
            val format = SimpleDateFormat("LLLL", Locale("en"))
            return format.format(calendar.time).replaceFirstChar { it.uppercaseChar() }
        }

        fun getShortMonthName(month: Int): String {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.MONTH, month - 1)
            val format = SimpleDateFormat("LLL", Locale("ru"))
            return format.format(calendar.time).replaceFirstChar { it.uppercaseChar() }
        }

    }
}