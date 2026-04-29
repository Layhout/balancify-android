package com.example.balancify.presentation.expense_form.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.balancify.core.constant.BG_COLORS
import com.example.balancify.core.constant.BORDER_RADIUS_MD
import com.example.balancify.core.constant.BORDER_RADIUS_SM
import com.example.balancify.domain.model.ExpenseIcon
import com.example.balancify.presentation.expense_form.ExpenseFormAction
import com.example.balancify.presentation.expense_form.ExpenseFormViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseIconFormBottomSheet(
    viewModel: ExpenseFormViewModel = koinViewModel(),
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val state = viewModel.state.collectAsStateWithLifecycle()

    val girdGap = 10.dp

    val selectedIconIndex = state.value.icon.ordinal
    val selectedColorIndex = BG_COLORS.indexOf(state.value.iconBgColor)

    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = Color(state.value.iconBgColor.toColorInt()),
                        shape = RoundedCornerShape(
                            size = BORDER_RADIUS_MD,
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    ImageVector.vectorResource(
                        state.value.icon.id
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("Icons", style = MaterialTheme.typography.labelLarge)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(girdGap),
                    verticalArrangement = Arrangement.spacedBy(girdGap),
                ) {
                    ExpenseIcon.entries.forEachIndexed { index, icon ->
                        Box(
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        viewModel.onAction(ExpenseFormAction.OnIconChange(icon))
                                    }
                                )
                                .size(44.dp)
                                .background(
                                    color = if (index == selectedIconIndex)
                                        MaterialTheme.colorScheme.outlineVariant
                                    else Color.Transparent,
                                    shape = RoundedCornerShape(
                                        size = BORDER_RADIUS_SM,
                                    )
                                )
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                ImageVector.vectorResource(
                                    icon.id
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(36.dp),
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("Colors", style = MaterialTheme.typography.labelLarge)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(girdGap),
                    verticalArrangement = Arrangement.spacedBy(girdGap),
                ) {
                    BG_COLORS.forEachIndexed { index, colorString ->
                        Box(
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        viewModel.onAction(
                                            ExpenseFormAction.OnIconBgColorChange(
                                                colorString
                                            )
                                        )
                                    }
                                )
                                .size(44.dp)
                                .background(
                                    color = if (index == selectedColorIndex)
                                        MaterialTheme.colorScheme.outlineVariant
                                    else Color.Transparent,
                                    shape = RoundedCornerShape(
                                        size = BORDER_RADIUS_SM,
                                    )
                                )
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = Color(colorString.toColorInt()),
                                        shape = RoundedCornerShape(
                                            size = BORDER_RADIUS_SM - 4.dp,
                                        )
                                    ),
                            )
                        }
                    }
                }
            }
        }
    }
}