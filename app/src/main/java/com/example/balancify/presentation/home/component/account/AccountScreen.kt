package com.example.balancify.presentation.home.component.account

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.BuildConfig
import com.example.balancify.core.util.ObserveAsEvents
import com.example.balancify.presentation.home.component.account.component.LogoutBottomSheet
import com.example.balancify.presentation.home.component.account.component.LogoutCard
import com.example.balancify.presentation.home.component.account.component.OptionCard
import com.example.balancify.presentation.home.component.account.component.UserProFile
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = koinViewModel(),
    onLogoutComplete: () -> Unit,
    onNavigateToFriend: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is AccountEvent.OnLogoutSuccessful -> {
                onLogoutComplete()
            }

            is AccountEvent.OnLogoutError -> {
                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }

            is AccountEvent.OnNavigateToFriend -> {
                onNavigateToFriend()
            }
        }
    }

    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 64.dp)
        ) {
            UserProFile()
            Spacer(modifier = Modifier.height(48.dp))
            OptionCard()
            Spacer(modifier = Modifier.height(16.dp))
            LogoutCard()
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "v${BuildConfig.VERSION_NAME}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            if (state.value.isLogoutBottomSheetVisible) {
                LogoutBottomSheet()
            }
        }
    }
}