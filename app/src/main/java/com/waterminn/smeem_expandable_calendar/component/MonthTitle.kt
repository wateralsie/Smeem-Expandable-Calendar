package com.waterminn.smeem_expandable_calendar.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.waterminn.smeem_expandable_calendar.ui.theme.CollapsibleCalendarTheme
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun MonthTitle(
    selectedMonth: YearMonth,
    modifier: Modifier = Modifier
) {
    Text(
        text = selectedMonth.format(DateTimeFormatter.ofPattern("yyyy년 M월")),
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun MonthTitlePreview() {
    CollapsibleCalendarTheme {
        MonthTitle(
            selectedMonth = YearMonth.now(),
            modifier = Modifier.padding(vertical = 18.dp)
        )
    }
}