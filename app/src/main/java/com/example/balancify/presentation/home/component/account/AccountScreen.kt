package com.example.balancify.presentation.home.component.account

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.core.util.ObserveAsEvents
import com.example.balancify.presentation.home.component.account.component.LogoutBottomSheet
import com.example.balancify.presentation.home.component.account.component.LogoutCard
import com.example.balancify.presentation.home.component.account.component.OptionCard
import com.example.balancify.presentation.home.component.account.component.UserProFile
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = koinViewModel(),
    onLogoutConfirm: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            AccountEvent.OnLogoutSuccessful -> {
                coroutineScope.launch {
                    onLogoutConfirm()
                }
            }

            is AccountEvent.OnLogoutError -> {
                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            UserProFile()
            Spacer(modifier = Modifier.height(48.dp))
            OptionCard()
            Spacer(modifier = Modifier.height(16.dp))
            LogoutCard()

            if (state.value.isLogoutBottomSheetVisible) {
                LogoutBottomSheet()
            }
        }
    }
}