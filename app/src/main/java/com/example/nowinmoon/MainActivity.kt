package com.example.nowinmoon

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.example.nowinmoon.ui.MoonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    val vm: MainActivityViewModel by viewModels()

    val titles = listOf("계산기", "숫자를 눌러보세용")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MoonTheme {
                // A surface container using the 'background' color from the theme
                MyApp()
            }
        }
    }

    @Composable
    fun MyApp(modifier: Modifier = Modifier) {

        var shouldShowOnBoarding by remember { mutableStateOf(true) }

        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.background
        ) {
            if (shouldShowOnBoarding) {
                OnboardingScreen( onContinueClicked = { shouldShowOnBoarding = false } )
            } else {
                Column {
                    Greeting(titles, modifier.padding(10.dp))
                    Greeting(titles, modifier.padding(10.dp))
                }
            }
        }
    }

    // 함수는 기본적으로 빈 수정자가 할당되는 수정자 매개변수를 포함하는 것이 좋습니다.?

    // 구성 가능한 함수는 Kotlin의 다른 함수처럼 사용할 수 있습니다.
    // 이는 UI가 표시되는 방식에 영향을 주는 구문을 추가할 수 있으므로 매우 강력한 UI를 제작할 수 있게 해줍니다. -> ???
    // 예를 들어, for 루프를 사용하여 Column에 요소를 추가할 수 있습니다.

    // 코드에서 눌러도 포커싱이 안간다. 프리뷰에서 누르면 코드로 이동.
    @Composable
    private fun Greeting(titles: List<String>, modifier: Modifier = Modifier) {
        var expanded by remember {   // remember : 리컴포지션을 방지하고 상태를 기억하게 해줌
            mutableStateOf(false)       // 컴포저블 내부 상태. 이 상태를 구독함. 상태가 변경되면 재구성됨.
        }

        val extraPadding = if (expanded) 48.dp else 0.dp

        Surface(color = MaterialTheme.colorScheme.primary) {
            Row(modifier.padding(24.dp)) {
                Column(modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding)
                ) {
                    for (item in titles) {
                        Text(
                            text = item,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(1.dp)
                        )
                    }
                }
                ElevatedButton(
                    onClick = { expanded = !expanded }
                ) {
                    Text(
                        if (expanded)
                            "Show less!"
                        else
                            "Show more!"
                    )
                }
            }
        }
    }

    @Preview(showBackground = true, widthDp = 320)
    @Composable
    fun Preview() {
        MoonTheme {
            MyApp()
        }
    }

    @Composable
    fun OnboardingScreen(onContinueClicked: () -> Unit, modifier: Modifier = Modifier) {

        Column (
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Welcome to the NowInMoon!!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClicked
            ) {
                Text("Continue")
            }
        }
    }

    @Preview(showBackground = true, widthDp = 320, heightDp = 320)
    @Composable
    fun OnBoardingPreview() {
        MoonTheme {
            OnboardingScreen(onContinueClicked = {})    // do nothing on click.
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}