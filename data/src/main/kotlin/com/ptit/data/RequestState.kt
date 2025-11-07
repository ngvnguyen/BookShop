package com.ptit.data

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

sealed class RequestState<out T> {
    object IDLE : RequestState<Nothing>()
    object LOADING : RequestState<Nothing>()
    data class SUCCESS<out T>(val data:T): RequestState<T>()
    data class ERROR(val message:String): RequestState<Nothing>()

    fun isIdle() = this is IDLE
    fun isLoading() = this is LOADING
    fun isSuccess() = this is SUCCESS
    fun isError() = this is ERROR

    fun getSuccessData() = (this as SUCCESS).data
    fun getErrorMessage() = (this as ERROR).message
}

@Composable
fun <T >RequestState<T>.DisplayResult(
    modifier: Modifier = Modifier,
    onSuccess:(@Composable (T)->Unit)? =null,
    onError:(@Composable (String)->Unit)? =null,
    onIdle:(@Composable ()->Unit)? =null,
    onLoading:(@Composable ()->Unit)? =null,
    transitionSpec : ContentTransform? = fadeIn() togetherWith fadeOut(),
    containerColor: Color = Color.Unspecified
){
    AnimatedContent(
        targetState = this,
        transitionSpec = {transitionSpec?: (EnterTransition.None togetherWith ExitTransition.None)},
        modifier = modifier.background(color = containerColor)
    ) { requestState->
        when(requestState){
            is RequestState.SUCCESS -> {
                onSuccess?.invoke(requestState.getSuccessData())
            }

            is RequestState.ERROR -> {
                onError?.invoke(requestState.getErrorMessage())
            }

            is RequestState.IDLE -> {
                onIdle?.invoke()
            }

            is RequestState.LOADING -> {
                onLoading?.invoke()
            }
        }

    }
}