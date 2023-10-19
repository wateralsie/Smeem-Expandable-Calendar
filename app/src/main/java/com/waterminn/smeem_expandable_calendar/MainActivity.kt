package com.waterminn.smeem_expandable_calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.waterminn.smeem_expandable_calendar.ui.theme.CollapsibleCalendarTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CollapsibleCalendarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SmeemCalendar()
                }
            }
        }
    }

    @Composable
    fun SmeemCalendar() {
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }
        val scrollState = rememberScrollState()

        Column(Modifier.verticalScroll(scrollState)) {
            ExpandableCalendar(
                onDayClick = { selectedDate = it }
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun SmeemCalendarPreview() {
        CollapsibleCalendarTheme {
            SmeemCalendar()
        }
    }
}

