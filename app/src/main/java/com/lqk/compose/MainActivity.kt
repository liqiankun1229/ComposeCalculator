package com.lqk.compose

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.lqk.base.activity.BaseComposeActivity
import com.lqk.compose.R.drawable
import com.lqk.compose.ui.theme.OnlyComposeTheme
import com.lqk.compose.vm.MainViewModel
import com.lqk.data.MMKVHelper
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.dialog.permissionMapOnQ

class MainActivity : BaseComposeActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlyComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
        MMKVHelper.saveString("project", "CalculatorProject")
        val mainViewModel = MainViewModel()
        mainViewModel.loadPackage()
    }

    private val launchPermissionContract = RequestPermission()
    val launchPermission = registerForActivityResult(launchPermissionContract) {
        // ????????????
        Log.d(TAG, "launch: ???????????? $it")
    }

}

@Composable
fun Greeting(name: String) {
    val showHome = true
    if (showHome) {
        Home()
    } else {
        History()
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    OnlyComposeTheme {
//        Greeting("Android")
//    }
//}


var historyList = mutableListOf<String>()
var keyArray = arrayListOf(
    arrayOf("AC" to Color.Black, "DEL" to Color.Black, "%" to Color.Black, "/" to Color.Green),
    arrayOf("7" to Color.Black, "8" to Color.Black, "9" to Color.Black, "*" to Color.Green),
    arrayOf("4" to Color.Black, "5" to Color.Black, "6" to Color.Black, "-" to Color.Green),
    arrayOf("1" to Color.Black, "2" to Color.Black, "3" to Color.Black, "+" to Color.Green),
    arrayOf("." to Color.Black, "0" to Color.Black, "( )" to Color.Black, "=" to Color.Green),
)

// ??????
var nums = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

/**
 * ????????????
 */
fun isNums(char: Char): Boolean {
    for (num in nums) {
        if (num == char) {
            return true
        }
    }
    return false
}

/**
 * ????????????????????????
 */
fun autoSize(length: Int): TextUnit {
    return when {
        length < 10 -> {
            48.sp
        }
        length in 10..15 -> {
            42.sp
        }
        length in 16..30 -> {
            38.sp
        }
        length in 30..40 -> {
            32.sp
        }
        else -> {
            24.sp
        }
    }
}

@Preview
@Composable
fun Home() {
    val activity = LocalContext.current
    var mText by remember { mutableStateOf("0") }
    var mTextNum by remember { mutableStateOf("0") }
    var img = BitmapPainter(image = ImageBitmap(10, 10))

    val scrollState = rememberScrollState()
    // ?????? ???????????????
    // ??????
    Column(modifier = Modifier.fillMaxSize()) {
        // ?????????
        Row(
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "?????????",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically),
                textAlign = TextAlign.Left,
            )
            Image(
                painter = painterResource(id = drawable.ic_history),
                modifier = Modifier
                    .width(52.dp)
                    .height(52.dp)
                    .padding(10.dp),
                alignment = Alignment.CenterEnd,
                contentDescription = "??????"
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = Color.Black)
        ) { }
        // ?????????
        Column(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .horizontalScroll(scrollState)
            ) {
                Text(
                    text = mText,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.height(80.dp),
                    style = TextStyle(fontSize = autoSize(mText.length)),
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = mTextNum,
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(fontSize = 36.sp, textAlign = TextAlign.End)
                )
            }
        }
        // ?????????
        repeat(keyArray.size) { i ->
            Row(
                modifier = Modifier.weight(1f).aspectRatio(4f).also {
                    if (i == 0) {
                        it.padding(top = 10.dp)
                    }
                },
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(keyArray[i].size) { j ->
                    Column(modifier = Modifier.weight(1f)) {
                        Button(
                            onClick = {
                                val c = keyArray[i][j].first
                                when (c) {
                                    "AC" -> {
                                        mText = ""
                                        mTextNum = ""
                                    }
                                    "DEL" -> {
                                        if (mText != "") {
                                            mText = mText.substring(0, mText.length - 1)
                                        }
                                    }
                                    "+" -> {
                                        // ?????????
                                        mTextNum = MMKVHelper.gainString("project", "?????????")
                                    }
                                    "-", "*", "/" -> {
                                        // ?????????
                                        (activity as MainActivity).launchPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                        val checkActivity = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                                        val permissionActivity =
                                            ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                                        val permissionContext = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                                        val checkContext = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                                        Log.d(MainActivity.TAG, "Home: $checkActivity : $checkContext : $permissionActivity : $permissionContext")
                                    }
                                    "=" -> {
                                        // ??????
                                        mTextNum = mText
                                    }
                                    "%" -> {

                                    }
//                                    "." -> {
//
//                                    }
                                    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> {
                                        if (mText == "") {
                                            mText += c
                                        } else {
                                            if (isNums(mText.last())) {
                                                mText += c
                                            } else {
                                                if (isNums(c.last())) {
                                                    mText += c
                                                }
                                            }
                                        }
                                        // ???????????????
                                    }
                                    else -> {
                                        if (mText == "") {
                                            mText += c
                                        } else {
                                            if (isNums(mText.last())) {
                                                mText += c
                                            } else {
                                                if (isNums(c.last())) {
                                                    mText += c
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .weight(1f)
                                .aspectRatio(1f),
                            colors = ButtonDefaults.buttonColors(backgroundColor = keyArray[i][j].second)

                        ) {
                            Text(
                                text = keyArray[i][j].first,
                                style = TextStyle(
                                    fontSize = 36.sp,
                                    color = colorChange(keyArray[i][j].second)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

fun colorChange(color: Color) = Color(red = 255 - color.red.toInt(), green = 255 - color.green.toInt(), blue = 255 - color.blue.toInt())

/**
 * ?????? Button
 */
@Composable
fun CreateButton() {
}

@Composable
fun History() {

}
