package wrapContainers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpOffset

@Composable
fun DropdownMenuOnLongPressContainer(
    isEnabled: Boolean,
    onTap: (Offset) -> Unit = {},
    dropdownContent: @Composable (MutableState<Boolean>) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {

    val isDropdownExpanded = remember { mutableStateOf(false) }
    val offset: MutableState<DpOffset> = remember { mutableStateOf(DpOffset.Zero) }

    DropdownMenuContainer(
        isEnabled = isEnabled,
        isDropdownExpanded = isDropdownExpanded,
        offset = offset,
        dropdownContent = dropdownContent
    ) {
        LongPressContainer(
            isEnabled = isEnabled,
            onLongPress = {
                offset.value = it
                isDropdownExpanded.value = true
            },
            onTap = onTap
        ) {
            content()
        }
    }
}


@Composable
fun DropdownMenuContainer(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    offset: MutableState<DpOffset> = remember { mutableStateOf(DpOffset.Zero) },
    isDropdownExpanded: MutableState<Boolean>,
    dropdownContent: @Composable (MutableState<Boolean>) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {

    Box(modifier = modifier, contentAlignment = Alignment.TopStart) {
        content()
        // maybe it will be more optimized
//        if (isEnabled) {
            DropdownMenu(
                isEnabled && isDropdownExpanded.value,
                onDismissRequest = {
                    isDropdownExpanded.value = false
                },
                offset = offset.value,
                shape = MaterialTheme.shapes.large
            ) {
                dropdownContent(isDropdownExpanded)
            }
//        }
    }
}