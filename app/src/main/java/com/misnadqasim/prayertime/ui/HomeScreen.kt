package com.misnadqasim.prayertime.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.misnadqasim.prayertime.PreferencesManager
import com.misnadqasim.prayertime.models.TimingByCity
import com.misnadqasim.prayertime.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(paddingValues: PaddingValues) {

    val prayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")

    val times = remember { mutableStateListOf("5:00", "5:00", "5:00", "5:00", "5:00") }

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }

    val preferenceCity = PreferencesManager(LocalContext.current).getString(PreferencesManager.CITY)


    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Card( modifier = Modifier
                .padding(20.dp, 20.dp, 10.dp, 20.dp)
                .weight(1.0F, true) ) {
                Text(
                    text = "12 July 2024",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(10.dp, 30.dp)
                )
            }

            Card( modifier = Modifier
                .padding(10.dp, 20.dp, 20.dp, 20.dp)
                .weight(1.0F, true),
                onClick = { showDialog = true }) {
                Text(
                    text = preferenceCity,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(10.dp, 30.dp)
                )
            }
        }

        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 0.dp, 20.dp, 10.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                for (i in prayers.indices) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = prayers[i])
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(times[i])
                        }
                    }
                }
            }
        }

        if (showDialog) {
            CitySelectionDialog(
                oldCity = preferenceCity,
                onDismiss = { showDialog = false },
                onCitySelected = { city ->
                    PreferencesManager(context).saveString(PreferencesManager.CITY, city)
                    showDialog = false

                    coroutineScope.launch {
                        val data = getTimingByCity(context, preferenceCity, "India", 2)
                        if (data != null) {
                            times[0] = data.data.timings.Fajr
                            times[1] = data.data.timings.Dhuhr
                            times[2] = data.data.timings.Asr
                            times[3] = data.data.timings.Maghrib
                            times[4] = data.data.timings.Isha
                        }
                    }
                }
            )
        }

        LaunchedEffect(null) {
            coroutineScope.launch {
                val data = getTimingByCity(context, preferenceCity, "India", 2)
                if (data != null) {
                    times[0] = data.data.timings.Fajr
                    times[1] = data.data.timings.Dhuhr
                    times[2] = data.data.timings.Asr
                    times[3] = data.data.timings.Maghrib
                    times[4] = data.data.timings.Isha
                }
            }
        }
    }
}

@Composable
fun CitySelectionDialog(onDismiss: () -> Unit, onCitySelected: (String) -> Unit, oldCity: String) {
    val newCity = remember {
        mutableStateOf(oldCity)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Select your City") },
        text = {
            Column {
                TextField(value = newCity.value, onValueChange = {newCity.value = it} )
            }
        },
        confirmButton = {TextButton(onClick = { onCitySelected(newCity.value.trim()) }) {
            Text("Apply")
        }},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

suspend fun getTimingByCity(
    context: Context,
    city: String,
    country: String,
    method: Int
): TimingByCity? {
    try {
        val response: Response<TimingByCity> = RetrofitClient.apiService.getTimingByCity(
            city,
            country,
            method,
        )
        if (response.isSuccessful) {
            return response.body()
        } else {
            Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show()
            return null
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show()
        throw e
    }
}
