package com.needsvswants.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.needsvswants.app.ui.navigation.AppNavigation
import com.needsvswants.app.ui.theme.NeedsVsWantsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeedsVsWantsTheme {
                AppNavigation()
            }
        }
    }
}
