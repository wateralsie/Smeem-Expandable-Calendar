package com.waterminn.smeem_expandable_calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waterminn.smeem_expandable_calendar.core.CalendarIntent
import com.waterminn.smeem_expandable_calendar.core.Period
import com.waterminn.smeem_expandable_calendar.model.Date
import com.waterminn.smeem_expandable_calendar.util.getNextDates
import com.waterminn.smeem_expandable_calendar.util.getRemainingDatesInMonth
import com.waterminn.smeem_expandable_calendar.util.getRemainingDatesInWeek
import com.waterminn.smeem_expandable_calendar.util.getWeekStartDate
import com.waterminn.smeem_expandable_calendar.util.toYearMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class CalendarViewModel : ViewModel() {
    private val _diaryDateList = MutableStateFlow<List<LocalDate>>(
        listOf(
            LocalDate.of(2023, 10, 9),
            LocalDate.of(2023, 10, 19),
            LocalDate.of(2023, 10, 20),
            LocalDate.of(2023, 10, 21),
            LocalDate.of(2023, 10, 26),
            LocalDate.of(2023, 10, 28),
            LocalDate.of(2023, 11, 2),
        )
    )
    val diaryDateList: StateFlow<List<LocalDate>> = _diaryDateList

    private val _visibleDates =
        MutableStateFlow(
            calculateWeeklyCalendarDays(
                startDate = LocalDate.now().getWeekStartDate().minusWeeks(1)
            )
        )
    val visibleDates: StateFlow<Array<List<Date>>> = _visibleDates

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    val currentMonth: StateFlow<YearMonth>
        get() = isCalendarExpanded.zip(visibleDates) { isExpanded, dates ->
            when {
                isExpanded -> dates[1][dates[1].size / 2].day.toYearMonth()
                dates[1].count { it.day.month == dates[1][0].day.month } > 3 -> dates[1][0].day.toYearMonth()
                else -> dates[1][dates[1].size - 1].day.toYearMonth()
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, LocalDate.now().toYearMonth())

    private val _isCalendarExpanded = MutableStateFlow(false)
    val isCalendarExpanded: StateFlow<Boolean> = _isCalendarExpanded

    fun onIntent(intent: CalendarIntent) {
        when (intent) {
            CalendarIntent.ExpandCalendar -> {
                calculateCalendarDates(
                    startDate = currentMonth.value.minusMonths(1).atDay(1),
                    period = Period.MONTH
                )
                _isCalendarExpanded.value = true
            }

            CalendarIntent.CollapseCalendar -> {
                calculateCalendarDates(
                    startDate = calculateWeeklyCalendarVisibleStartDay()
                        .getWeekStartDate()
                        .minusWeeks(1),
                    period = Period.WEEK
                )
                _isCalendarExpanded.value = false
            }

            is CalendarIntent.LoadNextDates -> {
                calculateCalendarDates(intent.startDate, intent.period)
            }

            is CalendarIntent.SelectDate -> {
                viewModelScope.launch {
                    _selectedDate.emit(intent.date)
                }
            }
        }
    }

    private fun calculateCalendarDates(
        startDate: LocalDate,
        period: Period = Period.WEEK
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _visibleDates.emit(
                when (period) {
                    Period.WEEK -> calculateWeeklyCalendarDays(startDate)
                    Period.MONTH -> calculateMonthlyCalendarDays(startDate)
                }
            )
        }
    }

    private fun calculateWeeklyCalendarVisibleStartDay(): LocalDate {
        val halfOfMonth = visibleDates.value[1][visibleDates.value[1].size / 2]
        val visibleMonth = YearMonth.of(halfOfMonth.day.year, halfOfMonth.day.month)
        return if (selectedDate.value.month == visibleMonth.month && selectedDate.value.year == visibleMonth.year)
            selectedDate.value
        else visibleMonth.atDay(1)
    }

    private fun calculateWeeklyCalendarDays(startDate: LocalDate): Array<List<Date>> {
        val dateList = mutableListOf<Date>()
        startDate.getNextDates(21).map {
            dateList.add(Date(it, true, it in diaryDateList.value))
        }
        return Array(3) {
            dateList.slice(it * 7 until (it + 1) * 7)
        }
    }

    private fun calculateMonthlyCalendarDays(startDate: LocalDate): Array<List<Date>> {
        return Array(3) { monthIndex ->
            val monthFirstDate = startDate.plusMonths(monthIndex.toLong())
            val monthLastDate = monthFirstDate.plusMonths(1).minusDays(1)

            monthFirstDate.getWeekStartDate().let { weekBeginningDate ->
                if (weekBeginningDate != monthFirstDate) {
                    weekBeginningDate.getRemainingDatesInMonth().map {
                        Date(it, false, it in diaryDateList.value)
                    }
                } else {
                    listOf()
                } +
                        monthFirstDate.getNextDates(monthFirstDate.month.length(monthFirstDate.isLeapYear)).map {
                            Date(it, true, it in diaryDateList.value)
                        } +
                        monthLastDate.getRemainingDatesInWeek().map {
                            Date(it, false, it in diaryDateList.value)
                        }
            }
        }
    }
}