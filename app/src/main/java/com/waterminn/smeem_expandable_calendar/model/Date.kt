package com.waterminn.smeem_expandable_calendar.model

import java.time.LocalDate

data class Date(
    val day: LocalDate,
    val isCurrentMonth: Boolean,
    val isDiaryWritten: Boolean
)
