package com.example.nowinmoon

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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

        var shouldShowOnBoarding by rememberSaveable { mutableStateOf(true) }
        var title = "test"

        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.background
        ) {
            if (shouldShowOnBoarding) {
                OnboardingScreen( onContinueClicked = { shouldShowOnBoarding = false } )
            } else {
                Column {
//                    Greeting(title, modifier.padding(10.dp))
                    Greetings()
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
    private fun Greeting(title: String, modifier: Modifier = Modifier) {
        Card(
           colors = CardDefaults.cardColors(
               containerColor = MaterialTheme.colorScheme.primary
           ),
            modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            CardContent(title)
        }
    }

    @Composable
    private fun CardContent(title: String, modifier: Modifier = Modifier) {
        // remember : 리컴포지션을 방지하고 상태를 기억하게 해줌
        // rememberSaveable : 화면이 회전되어도 유지.
        var expanded by rememberSaveable {
            mutableStateOf(false)       // 컴포저블 내부 상태. 이 상태를 구독함. 상태가 변경되면 재구성됨.
        }

        val extraPadding by animateDpAsState(
            if (expanded) 100.dp else 0.dp,
            // spring, tween, repeatable..
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,     // damping : 제동
                stiffness = Spring.StiffnessLow     // stiffness : "뻣뻣함"
            )
        )

        Surface(color = MaterialTheme.colorScheme.primary) {
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp)
//                        .padding(bottom = extraPadding.coerceAtLeast(0.dp))     // coerce : "강요하다", "억압하다"
                ) {
                    Text(
                        text = title,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(1.dp),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Red
                        )
                    )
                    if (expanded) {
                        Text(
                            text = "긴 문자여어어어러얼 입니다요. 엄청나게 길어요오오오오 완전 길어요오오오 테스트 하는 중입니다.".repeat(4)
                        )
                    }
                }
                // 리플 효과를 알아서 넣어줌.
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Filled.KeyboardArrowUp else Filled.KeyboardArrowDown,
                        contentDescription = if (expanded)
                            stringResource(R.string.show_less)
                        else
                            stringResource(R.string.show_more)
                    )
                }
                ElevatedButton(
                    onClick = { expanded = !expanded }
                ) {
                    Text(
                        if (expanded)
                            stringResource(R.string.show_less)
                        else
                            stringResource(R.string.show_more)
                    )
                }
            }
        }
    }

    @Composable
    private fun Greetings(
        modifier: Modifier = Modifier,
        names: List<String> = List(1000) { "$it" }
    ) {
        // LazyColumn은 RecyclerView와 같은 하위 요소를 재활용하지 않는다. 컴포저블을 방출하는 것은 Android View를 인스턴스화 하는것보다 상대적으로 비용이 적게 들므로
        // LazyColumn은 스크롤할 때 새 컴포저블을 방출하고 계속 성능을 유지한다.!
        LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
            // Android 스튜디오는 기본적으로 다른 items 함수를 선택하므로 androidx.compose.foundation.lazy.items를 가져와야 함
            items(items = names) { name ->  // items 자동으로 import 안됨.
                Greeting(title = name)
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
                modifier = Modifier.padding(24.dp),
                onClick = onContinueClicked
            ) {
                Text(
                    "Continue...?"
                )
            }
        }
    }

    @Preview(
        showBackground = true,
        widthDp = 320,
        heightDp = 320,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        name = "온보딩 프리뷰"
    )
    @Composable
    fun OnBoardingPreview() {
        MoonTheme {
            OnboardingScreen(onContinueClicked = {})    // do nothing on click.
        }
    }

    @Preview(
        showBackground = true,
        widthDp = 320,
        heightDp = 320,
        name = "온보딩 프리뷰 - 기본"
    )
    @Composable
    fun OnBoardingPreview2() {
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