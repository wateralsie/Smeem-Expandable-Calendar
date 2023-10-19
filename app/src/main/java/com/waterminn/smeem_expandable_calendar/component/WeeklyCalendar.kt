package com.waterminn.smeem_expandable_calendar.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.waterminn.smeem_expandable_calendar.datasource.weeklyDateList
import com.waterminn.smeem_expandable_calendar.ui.theme.CollapsibleCalendarTheme
import java.time.LocalDate

@Composable
internal fun WeeklyCalendar(
    dateList: Array<List<LocalDate>>,
    selectedDate: LocalDate,
    loadNextWeek: (nextWeekDate: LocalDate) -> Unit,
    loadPrevWeek: (endWeekDate: LocalDate) -> Unit,
    onDayClick: (LocalDate) -> Unit
) {
    val itemWidth = (LocalConfiguration.current.screenWidthDp.dp - 38.dp) / 7
    CalendarPager(
        dateList = dateList,
        loadNextDates = loadNextWeek,
        loadPrevDates = loadPrevWeek
    ) { currentPage ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 19.dp, end = 19.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            dateList[currentPage].forEach { date ->
                Box(
                    modifier = Modifier
                        .width(itemWidth),
                    contentAlignment = Alignment.Center
                ) {
                    DayItem(
                        date = date,
                        onDayClick = onDayClick,
                        isSelected = selectedDate == date
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun WeeklyCalendarPreview() {
    CollapsibleCalendarTheme {
        WeeklyCalendar(
            dateList = weeklyDateList,
            selectedDate = LocalDate.now(),
            loadNextWeek = {},
            loadPrevWeek = {},
            onDayClick = {}
        )
    }
}