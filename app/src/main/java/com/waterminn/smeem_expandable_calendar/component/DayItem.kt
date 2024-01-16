package com.waterminn.smeem_expandable_calendar.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waterminn.smeem_expandable_calendar.ui.theme.CollapsibleCalendarTheme
import com.waterminn.smeem_expandable_calendar.ui.theme.gray400
import java.time.LocalDate


/**
 * view that represent one day in a calendar
 * @param date: date that view should represent
 * @param isFirstWeek: flag that indicates if name of week day should be visible above day value
 * */
@Composable
fun DayItem(
    date: LocalDate,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isCurrentMonth: Boolean = true,
    isDiaryWritten: Boolean = true,
    isFirstWeek: Boolean = true,
    isSixWeeks: Boolean = false,
) {
    val isToday = date == LocalDate.now()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(
                top = when {
                    isFirstWeek -> 0.dp
                    isSixWeeks -> 13.6.dp
                    else -> 28.dp
                }
//                top = when {
//                    isFirstWeek -> 0.dp
//                    isSixWeeks -> 6.8.dp
//                    else -> 14.dp
//                },
//                bottom = when {
//                    isSixWeeks -> 6.8.dp
//                    else -> 14.dp
//                }
            )
    ) {
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    isToday -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.background
                },
            ),
            modifier = modifier
                .border(
                    width = 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(6.dp)
                )
                .aspectRatio(1f)
                .alpha(if (!isCurrentMonth) 0f else 1f)
                .clickable(
                    enabled = isCurrentMonth,
                    onClick = { onDayClick(date) }
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.titleSmall.copy(letterSpacing = (-0.72).sp),
                    color = when {
                        isToday -> MaterialTheme.colorScheme.onPrimary
                        isSelected || isDiaryWritten -> MaterialTheme.colorScheme.onBackground
                        else -> gray400
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun DayItemPreview() {
    CollapsibleCalendarTheme {
        DayItem(
            date = LocalDate.now().plusDays(0),
            onDayClick = {},
            isSelected = false,
            isCurrentMonth = true,
            isDiaryWritten = false,
            isFirstWeek = true,
            modifier = Modifier.widthIn(max = 36.dp)
        )
    }
}