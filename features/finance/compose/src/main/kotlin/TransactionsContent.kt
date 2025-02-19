import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import goals.GoalsComponent
import goals.GoalsStore
import transactions.TransactionsComponent
import transactions.TransactionsStore
import view.theme.Paddings

fun LazyListScope.transactionsContent(
    model: TransactionsStore.State,
    transactions: List<Transaction>,
    component: TransactionsComponent
) {
    item {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(
                onClick = {

                }
            ) {
                Icon(Icons.Rounded.Add, null)
            }
        }
    }

    itemsIndexed(transactions, key= {_, item -> "${item.id}${item.isEditing}"}) { index, item ->
        if (index != 0) {
            Spacer(Modifier.height(Paddings.medium))
        }

    }
}

@Composable
private fun TransactionCard(
    originalTransaction: Transaction,
    model: TransactionsStore.State,
    component: TransactionsComponent
) {

}