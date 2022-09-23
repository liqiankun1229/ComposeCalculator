package com.lqk.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lqk.compose.R.drawable
import com.lqk.compose.ui.theme.OnlyComposeTheme

class MainActivity : ComponentActivity() {
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

// 数字
var nums = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

/**
 * 验证数字
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
 * 字体大小动态变化
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
    var mText by remember { mutableStateOf("0") }
    var mTextNum by remember { mutableStateOf("0") }
    var img = BitmapPainter(image = ImageBitmap(10, 10))

    val scrollState = rememberScrollState()
    // 显示 计算机主页
    // 标题
    Column(modifier = Modifier.fillMaxSize()) {
        // 标题区
        Row(
            modifier = Modifier
                .height(52.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "计算器",
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
                contentDescription = "历史"
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = Color.Black)
        ) { }
        // 计算区
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
        // 按键区
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
                                    "+", "-", "*", "/" -> {
                                        // 运算符
                                    }
                                    "=" -> {
                                        // 计算
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
                                        // 滑动到尾部
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
 * 创建 Button
 */
@Composable
fun CreateButton() {
}

@Composable
fun History() {

}
