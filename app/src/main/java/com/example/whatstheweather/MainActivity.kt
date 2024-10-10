package com.example.whatstheweather

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.whatstheweather.ui.theme.WhatsTheWeatherTheme

class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            WhatsTheWeatherTheme {


                val errorMessage by weatherViewModel.errorMessage.observeAsState()


                errorMessage?.let {

                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                }


                SearchScreen(viewModel = weatherViewModel)

            }
        }
    }
}
