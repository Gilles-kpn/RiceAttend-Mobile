package fr.gilles.riceattend.ui.widget.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun IncludeLottieFile(draw: Int, modifier: Modifier, iterations: Int = LottieConstants.IterateForever) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(draw))
    LottieAnimation(
        composition,
        iterations = iterations,
        modifier = modifier
    )
}



