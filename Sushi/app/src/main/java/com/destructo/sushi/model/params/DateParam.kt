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
            return "${getAbbreviatedMonth(str[1].toInt())}-${ getDateAsTwoDecimalPlaces(str[2].toInt()) }-${str[0]}"
        }

        fun fromFormattedToDateParam(date: String): DateParam{
            return if(isValidMalDate(date)){
                val str = date.split("-")
                DateParam(
                    month = fromAbbreviatedMonthToInt(str[0]),
                    year = str[2],
                    day = getDateAsTwoDecimalPlaces(str[1].toInt())
                )
            }else getTodayAsDateParam()

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

        private fun getTodayAsDateParam(): DateParam{
            val day = getDateAsTwoDecimalPlaces(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            val month = Calendar.getInstance().get(Calendar.MONTH).plus(1).toString()
            val year = Calendar.getInstance().get(Calendar.YEAR).toString()

            return DateParam(day, month, year)
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

        private fun isValidMalDate(date: String): Boolean{
            val regex = Regex("""[A-z]{3}-[0-9]{2}-[0-9]{4}""")
            return regex.matches(date)
        }

        private fun getDateAsTwoDecimalPlaces(number: Int): String{
            return if (number < 10) "0$number" else "$number"
        }
    }

}
