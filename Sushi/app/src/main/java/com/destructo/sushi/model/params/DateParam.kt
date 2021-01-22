package com.destructo.sushi.model.params

import java.util.*

data class DateParam(
    val day: String,
    val month: String,
    val year: String
){

    companion object {
        fun getFormattedDate(date: String): String {
            val str = date.split("-")
            return "${getAbbreviatedMonth(str[1].toInt())}-${str[2]}-${str[0]}"
        }

        fun fromFormattedToDateParam(date: String): DateParam{
            val str = date.split("-")
            return DateParam(
                month = fromAbbreviatedMonthToInt(str[0]),
                year = str[2],
                day = str[1]
            )
        }

        fun fromFormattedToMalFormat(date: String): String{
            val str = date.split("-")
            return "${str[2]}-${fromAbbreviatedMonthToInt(str[0])}-${str[1]}"
        }

        fun getTodayFormatted(): String{
            val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val month = Calendar.getInstance().get(Calendar.MONTH).plus(1)
            val year = Calendar.getInstance().get(Calendar.YEAR)

            return "${getAbbreviatedMonth(month)}-${day}-${year}"
        }

        private fun getAbbreviatedMonth(month: Int): String{
            return when(month){
                1 -> "JAN"
                2 -> "FEB"
                3 -> "MAR"
                4 -> "APR"
                5 -> "MAY"
                6 -> "JUN"
                7 -> "JUL"
                8 -> "AUG"
                9 -> "SEP"
                10 -> "OCT"
                11 -> "NOV"
                12 -> "DEC"

                else -> "JAN"
            }
        }

        private fun fromAbbreviatedMonthToInt(month: String): String{
            return when(month){
                "JAN" -> "01"
                "FEB" -> "02"
                "MAR" -> "03"
                "APR" -> "04"
                "MAY" -> "05"
                "JUN" -> "06"
                "JUL" -> "07"
                "AUG" -> "08"
                "SEP" -> "09"
                "OCT" -> "10"
                "NOV" -> "11"
                "DEC" -> "12"

                else -> "01"
            }
        }
    }

}
