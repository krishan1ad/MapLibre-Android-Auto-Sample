package nl.flitsmeister.car_common.extentions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Use this to run a block on the main Android thread.
 * This should be used only for interacting with the UI and performing quick work.
 *
 * Examples include calling suspend functions, running Android UI framework operations, and updating LiveData objects.
 */
fun runOnMainThread(block: suspend CoroutineScope.() -> Unit) =
    launchOnContext(Dispatchers.Main, block)

/**
 * Launch the given block on the given coroutine context
 * The scope will be cancelled after completion to prevent leaks
 */
fun launchOnContext(
    coroutineContext: CoroutineContext,
    block: suspend CoroutineScope.() -> Unit
): Job =
    CoroutineScope(coroutineContext).let { scope ->
        scope.launch(block = block).apply {
            invokeOnCompletion {
                scope.cancel()
            }
        }
    }