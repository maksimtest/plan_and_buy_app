package com.shoppingplanning

import android.content.Context
import android.graphics.Paint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderLine(text: String = "Shopping") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            color = colorResource(id = R.color.main_text_color)
        )
    }
}
@Composable
fun TitleLine(text: String = "Shopping") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            color = colorResource(id = R.color.main_text_color)
        )
    }
}


@Composable
fun NeumorphicBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFE0E0E0),
    cornerRadius: Dp = 10.dp,
    lightShadowColor: Color = Color.White,
    darkShadowColor: Color = Color(0xFFA3B1C6),
    shadowBlurRadius: Float = 20f,
    offset: Float = 10f,
    content: @Composable BoxScope.() -> Unit
) {
    val radiusPx = with(LocalDensity.current) { cornerRadius.toPx() }

    Box(
        modifier = modifier
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val paint = Paint().apply {
                        isAntiAlias = true
                        style = Paint.Style.FILL
                    }

                    // Left top light shadow
                    paint.setShadowLayer(
                        shadowBlurRadius,
                        -offset,
                        -offset,
                        lightShadowColor.toArgb()
                    )
                    canvas.nativeCanvas.drawRoundRect(
                        0f,
                        0f,
                        size.width,
                        size.height,
                        radiusPx,
                        radiusPx,
                        paint
                    )

                    // Right bottom dark shadow
                    paint.setShadowLayer(shadowBlurRadius, offset, offset, darkShadowColor.toArgb())
                    canvas.nativeCanvas.drawRoundRect(
                        0f,
                        0f,
                        size.width,
                        size.height,
                        radiusPx,
                        radiusPx,
                        paint
                    )
                }
            }
            .background(color = backgroundColor, shape = RoundedCornerShape(cornerRadius))
            .padding(vertical = 4.dp, horizontal = 10.dp),
        content = content
    )
}

@Composable
fun ShortNeumorphicBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFE0E0E0),
    cornerRadius: Dp = 6.dp,
    lightShadowColor: Color = Color.White,
    darkShadowColor: Color = Color(0xFFA3B1C6),
    shadowBlurRadius: Float = 14f,
    offset: Float = 6f,
    content: @Composable BoxScope.() -> Unit
) {
    val radiusPx = with(LocalDensity.current) { cornerRadius.toPx() }

    Box(
        modifier = modifier
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val paint = Paint().apply {
                        isAntiAlias = true
                        style = Paint.Style.FILL
                    }

                    // Left top light shadow
                    paint.setShadowLayer(
                        shadowBlurRadius,
                        -offset,
                        -offset,
                        lightShadowColor.toArgb()
                    )
                    canvas.nativeCanvas.drawRoundRect(
                        0f,
                        0f,
                        size.width,
                        size.height,
                        radiusPx,
                        radiusPx,
                        paint
                    )

                    // Right bottom dark shadow
                    paint.setShadowLayer(shadowBlurRadius, offset, offset, darkShadowColor.toArgb())
                    canvas.nativeCanvas.drawRoundRect(
                        0f,
                        0f,
                        size.width,
                        size.height,
                        radiusPx,
                        radiusPx,
                        paint
                    )
                }
            }
            .background(color = backgroundColor, shape = RoundedCornerShape(cornerRadius))
            .padding(vertical = 8.dp, horizontal = 8.dp),
        content = content
    )
}

@Composable
fun CreateButton(text: String, onclick: () -> Unit) {
    Button(
        onClick = {
            onclick()
        },
        modifier = Modifier
            .background(colorResource(id = R.color.main_background)),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
        enabled = true,
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.main_background)),
    ) {

        ShortNeumorphicBox(
            modifier = Modifier
                .padding(all = 2.dp),
        ) {
            Text(
                text = text,
                maxLines = 1,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Default,
                color = colorResource(id = R.color.black)
            )

        }
    }
}

@Composable
fun CreateTextField(inputTextState: MutableState<String>, placeholderText: String, widthPx: Dp) {
    NeumorphicBox(
        modifier = Modifier
            .width(widthPx),
    ) {

        BasicTextField(
            value = inputTextState.value,
            onValueChange = { inputTextState.value = it },
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = colorResource(id = R.color.main_text_color),
                fontWeight = FontWeight.Normal,
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (inputTextState.value.isEmpty()) {
                        Text(
                            text = placeholderText,
                            fontSize = 14.sp,
                            maxLines = 1,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            },

            modifier = Modifier
                .width(widthPx)
                .padding(vertical = 4.dp)
        )
    }
}
@Composable
fun IntegerTextField(countState: MutableIntState) {

    TextField(
        value = "${countState.value}",
        onValueChange = { newValue:String ->
            // Allow just digit
            if (newValue.all { it.isDigit() }) {
                countState.value = newValue.toIntOrNull() ?: 0
            }
        },
        placeholder = {Text("")},
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = colorResource(id = R.color.main_background),
                shape = RoundedCornerShape(8.dp)
            )

        ,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        singleLine = true
    )
}

fun showMsg(context:Context, msg:String){
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}
