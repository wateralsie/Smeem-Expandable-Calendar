package com.waterminn.smeem_expandable_calendar.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CalendarToggleButton(
    isExpanded: Boolean,
    expand: () -> Unit,
    collapse: () -> Unit
) {
    IconToggleButton(
        checked = isExpanded,
        onCheckedChange = { isChecked -> if (isChecked) expand() else collapse() }
    ) {
        when {
            isExpanded -> Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "collapse calendar", tint = MaterialTheme.colorScheme.onBackground)
            else -> Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "expand calendar", tint = MaterialTheme.colorScheme.onBackground)
        }
    }
}