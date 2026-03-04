package com.example.balancify.presentation.search.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.balancify.core.constant.BORDER_RADIUS_MD

@Composable
fun SearchTextField(
    onSearchClick: (value: String) -> Unit,
) {
    var textValue by rememberSaveable {
        mutableStateOf("")
    }

    OutlinedTextField(
        label = { Text("Search...") },
        value = textValue,
        onValueChange = { textValue = it },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(BORDER_RADIUS_MD),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchClick(textValue) }
        ),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
            )
        }
    )
}