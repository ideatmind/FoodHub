package com.foodhub.fooddelivery.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodhub.fooddelivery.R
import com.foodhub.fooddelivery.ui.theme.AppColor
import com.foodhub.fooddelivery.ui.theme.AppWhite
import com.foodhub.fooddelivery.ui.theme.poppinsFontFamily

@Composable
fun GroupSocialButtons(color: Color = Color.White,onFaceBookClick:()->Unit, onGoogleClick:()->Unit,) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            HorizontalDivider(modifier = Modifier.width(100.dp),thickness = 1.dp, color = color)
            Text(text = stringResource(id = R.string.signIn_tile), fontFamily = poppinsFontFamily, color = color, modifier = Modifier.padding(horizontal = 8.dp) )
            HorizontalDivider(modifier = Modifier.width(100.dp),thickness = 1.dp, color = color)
        }
        Spacer(Modifier.height(12.dp))

        //login with google and facebook button
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onFaceBookClick,
                modifier = Modifier
                    .height(95.dp)
                    .width(180.dp)
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppWhite)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(R.drawable.facebook), contentDescription = null, modifier = Modifier
                        .size(35.dp)
                        .padding(end = 5.dp))
                    Text(text = stringResource(id = R.string.facebook), style = TextStyle(letterSpacing = 1.sp), fontFamily = poppinsFontFamily, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
                }
            }

            Button(onClick = onGoogleClick,
                modifier = Modifier
                    .height(95.dp)
                    .width(180.dp)
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppWhite)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(R.drawable.google), contentDescription = null, modifier = Modifier
                        .size(35.dp)
                        .padding(end = 5.dp))
                    Text(text = stringResource(id = R.string.google), style = TextStyle(letterSpacing = 1.sp), fontFamily = poppinsFontFamily, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
                }
            }
        }
    }
}


@Composable
fun FoodHubTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(10.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors().copy(
        unfocusedIndicatorColor = Color.LightGray.copy(alpha = 0.4f),
        focusedIndicatorColor = AppColor,
        cursorColor = AppColor,
        focusedTextColor = Color.DarkGray,
        unfocusedTextColor = Color.Gray
    )
) {
    Column (Modifier.padding(vertical = 8.dp)){
        label?.let {
            Row {
                Spacer(modifier = Modifier.size(8.dp))
                it()
            }
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.copy(fontWeight = FontWeight.SemiBold),
            label = null,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            colors = colors,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            interactionSource = interactionSource,
            shape = shape,
            isError = isError
        )
    }
}

@Composable
fun BasicDialog(title: String, description: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = description,
            color = Color.DarkGray
        )
        Spacer(Modifier.size(16.dp))
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = AppColor),
            shape = RoundedCornerShape(32.dp)
        ) {
            Text(text = stringResource(R.string.ok))
        }
    }
}
