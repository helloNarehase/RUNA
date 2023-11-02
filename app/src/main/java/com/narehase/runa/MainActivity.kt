package com.narehase.runa

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.narehase.runa.ui.theme.RUNATheme
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity(), ModelHelper.Luna {
    lateinit var modelHelper: ModelHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modelHelper = ModelHelper(
            this,
            this.assets
        )
        setContent {
            RUNATheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            modelHelper.Anyway()
                        },
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Greeting("Android")
                }
            }
        }
    }
    override fun onResult(results: Array<FloatArray>, inferenceTime: Long) {
        Log.e("Result", "${results[0].toList()} | inferenceTime(ms) ->| ${inferenceTime} |<- ")
    }
    override fun connected(tf:Boolean) {
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RUNATheme {
        Greeting("Android")
    }
}