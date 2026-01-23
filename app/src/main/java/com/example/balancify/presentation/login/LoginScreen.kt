package com.example.balancify.presentation.login

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowDpSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.R
import com.example.balancify.ui.theme.BalancifyTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun LoginScreen(
    onLoginComplete: () -> Unit = {},
    viewModel: LoginViewModel = koinViewModel<LoginViewModel>()
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()
    val isLoading = state.value.isLoading

    LaunchedEffect(null) {
        viewModel.loadSignInBottomSheet(context, onLoginComplete)
    }

    val density = LocalDensity.current

    val screenWidthPx = with(density) {
        currentWindowDpSize().width.roundToPx()
    }
    val smallDimension = minOf(
        currentWindowDpSize().width,
        currentWindowDpSize().height
    )
    val smallDimensionPx = with(density) {
        smallDimension.roundToPx()
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val isAtLeastAndroid12 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isAtLeastAndroid12) {
                        Modifier.blur(smallDimension / 3f)
                    } else Modifier
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (isAtLeastAndroid12) primaryColor else primaryColor.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.background
                        ),
                        center = Offset(
                            x = screenWidthPx / 2f,
                            y = -100f
                        ),
                        radius = smallDimensionPx / 2f
                    )
                )
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        ImageVector.vectorResource(if (isSystemInDarkTheme()) R.drawable.balancify_logo_dark else R.drawable.balancify_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.width(80.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Balancify",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Welcome to Balancify",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "A clean, no-fuss way to manage shared expenses with anyone, anywhere. Because fairness shouldn’t be complicated.",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { viewModel.onClickSignIn(context, onLoginComplete) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text("Sign In")
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    BalancifyTheme {
        LoginScreen()
    }
}
