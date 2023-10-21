package com.waterminn.smeem_expandable_calendar

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.waterminn.smeem_expandable_calendar.component.CalendarToggleButton
import com.waterminn.smeem_expandable_calendar.component.MonthTitle
import com.waterminn.smeem_expandable_calendar.component.MonthlyCalendar
import com.waterminn.smeem_expandable_calendar.component.WeekLabel
import com.waterminn.smeem_expandable_calendar.component.WeeklyCalendar
import com.waterminn.smeem_expandable_calendar.core.CalendarIntent
import com.waterminn.smeem_expandable_calendar.core.Period
import com.waterminn.smeem_expandable_calendar.util.getWeekStartDate
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun ExpandableCalendar(
    onDayClick: (LocalDate) -> Unit
) {
    val viewModel: CalendarViewModel = viewModel()
    val dateList = viewModel.visibleDates.collectAsState()
    val selectedDate = viewModel.selectedDate.collectAsState()
    val isCalendarExpanded = viewModel.isCalendarExpanded.collectAsState()
    val currentMonth = viewModel.currentMonth.collectAsState()

    ExpandableCalendar(
        dateList = dateList.value,
        selectedDate = selectedDate.value,
        currentMonth = currentMonth.value,
        onIntent = viewModel::onIntent,
        isCalendarExpanded = isCalendarExpanded.value,
        onDayClick = onDayClick
    )
}

@Composable
private fun ExpandableCalendar(
    dateList: Array<List<LocalDate>>,
    selectedDate: LocalDate,
    currentMonth: YearMonth,
    onIntent: (CalendarIntent) -> Unit,
    isCalendarExpanded: Boolean,
    onDayClick: (LocalDate) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.animateContentSize()
    ) {
        MonthTitle(
            selectedMonth = currentMonth,
            modifier = Modifier.padding(vertical = 18.dp)
        )
        WeekLabel()
        if (isCalendarExpanded) {
            MonthlyCalendar(
                dateList = dateList,
                selectedDate = selectedDate,
                currentMonth = currentMonth,
                loadDatesForMonth = { yearMonth ->
                    onIntent(
                        CalendarIntent.LoadNextDates(
                            startDate = yearMonth.atDay(1),
                            period = Period.MONTH
                        )
                    )
                },
                onDayClick = {
                    onIntent(CalendarIntent.SelectDate(it))
                    onDayClick(it)
                }
            )
        } else {
            WeeklyCalendar(
                dateList = dateList,
                selectedDate = selectedDate,
                loadNextWeek = { nextWeekDate -> onIntent(CalendarIntent.LoadNextDates(nextWeekDate)) },
                loadPrevWeek = { endWeekDate -> onIntent(CalendarIntent.LoadNextDates(endWeekDate.minusDays(1).getWeekStartDate())) },
                onDayClick = {
                    onIntent(CalendarIntent.SelectDate(it))
                    onDayClick(it)
                }
            )
        }
        CalendarToggleButton(
            isExpanded = isCalendarExpanded,
            expand = { onIntent(CalendarIntent.ExpandCalendar) },
            collapse = { onIntent(CalendarIntent.CollapseCalendar) }
        )
    }
}
