package com.example.catapp.core.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatInstantToDateString(
    instant: Instant,
    pattern: String = "yyyy-MM-dd HH:mm:ss",
    zoneId: ZoneId = ZoneId.systemDefault(),
): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
        .withLocale(Locale.getDefault())
        .withZone(zoneId)
    return formatter.format(instant)
}
