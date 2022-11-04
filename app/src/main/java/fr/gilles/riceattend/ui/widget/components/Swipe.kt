package fr.gilles.riceattend.ui.widget.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Swipe(
    content: @Composable () -> Unit,
    onSwipeToLeft: () -> Unit = {},
    onSwipeToRight: () -> Unit = {},
    leftContent: @Composable () -> Unit = {},
    rightContent: @Composable () -> Unit = {},
    loading : Boolean = false,
    sweepable: Boolean = false
) {
    val dismissState = rememberDismissState(initialValue = DismissValue.Default)
    val scope = rememberCoroutineScope()
    SwipeToDismiss(
        state = dismissState,
        background = {
            if (sweepable)
            when (dismissState.dismissDirection) {
                DismissDirection.EndToStart -> {
                    if (loading){
                        CircularProgressIndicator(

                        )
                    }else{
                        rightContent()
                        onSwipeToLeft().also {
                            scope.launch {
                                dismissState.reset()
                            }
                        }
                    }
                }
                DismissDirection.StartToEnd -> {
                    if (loading){
                        CircularProgressIndicator()
                    }else{
                        leftContent()
                        onSwipeToRight().also {
                            scope.launch {
                                dismissState.reset()
                            }
                        }
                    }
                }
                else -> {
                    content()
                }
            }
        },
        dismissContent = { content() },
        directions = if(sweepable) setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd) else setOf(),
    )


}