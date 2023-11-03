package com.waterminn.smeem_expandable_calendar.util

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

/**
 * @return YearMonth object of given date
 * */
internal fun LocalDate.toYearMonth(): YearMonth = YearMonth.of(this.year, this.month)

/**
 * @return list of [@param count] next dates
 * */
internal fun LocalDate.getNextDates(count: Int): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    repeat(count) { day ->
        dates.add(this.plusDays(day.toLong()))
    }
    return dates
}

/**
 * @return start date of week - sunday
 * */
internal fun LocalDate.getWeekStartDate(start: DayOfWeek = DayOfWeek.SUNDAY): LocalDate {
    var date = this
    while (date.dayOfWeek != start) {
        date = date.minusDays(1)
    }
    return date
}

/**
 * @return first date of month
 * */
internal fun LocalDate.getMonthStartDate(): LocalDate =
    LocalDate.of(this.year, this.month, 1)

/**
 * @return list of dates remaining in the week
 * */
internal fun LocalDate.getRemainingDatesInWeek(start: DayOfWeek = DayOfWeek.SUNDAY): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    var date = this.plusDays(1)
    while (date.dayOfWeek != start) {
        dates.add(date)
        date = date.plusDays(1)
    }
    return dates
}

/**
 * @return list of dates remaining in the month
 * this doesn't show in monthly calendar
 * */
internal fun LocalDate.getRemainingDatesInMonth(): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    repeat(this.month.length(this.isLeapYear) - this.dayOfMonth + 1) {
        dates.add(this.plusDays(it.toLong()))
    }
    return dates
}