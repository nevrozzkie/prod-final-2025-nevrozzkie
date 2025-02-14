package base

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import view.theme.Paddings

//@Composable
//fun CLazyColumn(
//    modifier: Modifier = Modifier,
//    state: LazyListState = rememberLazyListState(),
//    content: LazyListScope.() -> Unit
////    refreshState: PullToRefreshState
//) {
//    LazyColumn(
//        modifier
//            .padding(horizontal = Paddings.hMainContainer)
//            .safeContentPadding(),
//        state = state
//    ) {
//        content()
//    }
//}