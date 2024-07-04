package com.misnadqasim.prayertime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import com.misnadqasim.prayertime.ui.HomeScreen
import com.misnadqasim.prayertime.ui.theme.PrayerTimeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val selectedItem = remember { mutableIntStateOf(0) }

            PrayerTimeTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Prayer Time") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            NavigationBar {
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                                    label = { Text("Home") },
                                    selected = selectedItem.intValue == 0,
                                    onClick = { selectedItem.intValue = 0 }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Notifications, contentDescription = "Alarm") },
                                    label = { Text("Alarm") },
                                    selected = selectedItem.intValue == 1,
                                    onClick = { selectedItem.intValue = 1 }
                                )
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                                    label = { Text("Settings") },
                                    selected = selectedItem.intValue == 2,
                                    onClick = { selectedItem.intValue = 2 }
                                )
                            }
                        }
                    },
                    content = {
                        HomeScreen(it)
                    }
                )
            }
        }
    }
}