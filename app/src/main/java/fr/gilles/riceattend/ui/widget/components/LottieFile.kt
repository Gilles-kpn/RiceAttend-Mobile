package fr.gilles.riceattend.ui.widget.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun IncludeLottieFile(draw: Int, modifier: Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(draw))
    LottieAnimation(
        composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}



