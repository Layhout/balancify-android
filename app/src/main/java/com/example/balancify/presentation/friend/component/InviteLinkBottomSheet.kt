package com.example.balancify.presentation.friend.component

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.R
import com.example.balancify.core.constant.BORDER_RADIUS_SM
import com.example.balancify.presentation.friend.FriendAction
import com.example.balancify.presentation.friend.FriendViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteLinkBottomSheet(
    viewModel: FriendViewModel = koinViewModel()
) {
    val sheetState = rememberModalBottomSheetState()
    val clipboard = LocalClipboard.current
    rememberCoroutineScope()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, state.value.inviteLink)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)

    ModalBottomSheet(
        onDismissRequest = {
            viewModel.onAction(FriendAction.OnDismissInviteLinkBottomSheet)
        },
        sheetState = sheetState
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Image(
                ImageVector.vectorResource(R.drawable.add_person),
                contentDescription = "Logo",
                modifier = Modifier.width(52.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Invite Friends",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Share this link to invite your friend to join Balancify.",
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            viewModel.onAction(
                                FriendAction.OnInviteLinkClick(
                                    clipboard
                                )
                            )
                        },
                    )
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(
                            BORDER_RADIUS_SM
                        )
                    )
                    .padding(vertical = 8.dp, horizontal = 10.dp),
            ) {
                Text(
                    state.value.inviteLink,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    if (state.value.isInviteLinkCopied) Icons.Outlined.Check
                    else Icons.Outlined.ContentCopy,
                    modifier = Modifier.size(16.dp),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    context.startActivity(shareIntent)
                }
            ) { Text("Share") }

        }
    }
}