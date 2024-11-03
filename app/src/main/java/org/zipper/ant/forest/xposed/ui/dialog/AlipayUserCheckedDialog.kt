package org.zipper.ant.forest.xposed.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.zipper.antforestx.data.bean.AlipayUser

@Composable
fun AlipayUserCheckedDialog(
    userList: List<AlipayUser>,
    initCheckedUsers: List<AlipayUser>,
    onDismiss: (finalCheckedUsers: List<AlipayUser>) -> Unit
) {
    val checkedUsers = remember(initCheckedUsers) {
        mutableStateListOf<AlipayUser>(*initCheckedUsers.toTypedArray())
    }

    CustomDialog(
        title = "好友列表",
        onDismiss = {
            onDismiss.invoke(checkedUsers)
        },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {

            items(userList.size) { index ->
                UserCheckBoxItem(
                    user = userList[index],
                    checked = checkedUsers.contains(userList[index]),
                    onCheckedChange = { checked ->
                        if (checked) {
                            checkedUsers.add(userList[index])
                        } else {
                            checkedUsers.remove(userList[index])
                        }
                    })
            }
        }
    }
}


@Composable
private fun UserCheckBoxItem(
    modifier: Modifier = Modifier,
    user: AlipayUser,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = user.displayName)
        Spacer(modifier = Modifier.width(4.dp))
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
    }
}
