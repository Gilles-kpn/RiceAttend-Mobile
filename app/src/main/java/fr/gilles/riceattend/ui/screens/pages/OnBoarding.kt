package fr.gilles.riceattend.ui.screens.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.*
import fr.gilles.riceattend.R
import fr.gilles.riceattend.services.storage.RepositoryType
import fr.gilles.riceattend.services.storage.SessionManager
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.widget.components.IncludeLottieFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    nav: NavHostController
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    HorizontalPager(
        count = 2,
        Modifier.fillMaxSize(),
    ) { page: Int ->
        when (page) {
            0 -> WelcomePage(pagerState = pagerState, scope = scope)
            1 -> NetworkPage(pagerState = pagerState, scope = scope, onFinish = {
                nav.navigate(
                    when (SessionManager.session.repositoryType) {
                        RepositoryType.NONE -> Route.OnBoardingRoute.path
                        RepositoryType.REMOTE -> {
                            if (SessionManager.session.user != null) Route.MainRoute.path
                            else Route.AuthRoute.path
                        }
                        RepositoryType.LOCAL -> Route.MainRoute.path
                    }
                ) {
                    popUpTo(Route.OnBoardingRoute.path) {
                        inclusive = true
                    }
                }
            })
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomePage(pagerState: PagerState, scope: CoroutineScope) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IncludeLottieFile(
            draw = R.raw.welcome,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 8.dp)
        )
        Text(text = "Bienvenue sur RiceAttend", style = MaterialTheme.typography.h4)
        Text(
            text = "RiceAttend est une application créée pour les différents producteurs agricoles de riz afin de faciliter la gestion de leurs employés, des différentes tâches, ressources et champs de riz.",
            style = MaterialTheme.typography.body1
        )
        Row(
            modifier = Modifier.padding(10.dp)
                .clip(CircleShape).clickable(onClick = {
                scope.launch {
                    pagerState.scrollToPage(2)
                }
            }).padding(10.dp).align(Alignment.End),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Suivant", style = MaterialTheme.typography.body1, color = MaterialTheme.colors.primary)
            Icon(Icons.Outlined.ChevronRight, contentDescription = "Next", tint = MaterialTheme.colors.primary)
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun NetworkPage(
    onFinish: () -> Unit = {},
    pagerState: PagerState,
    scope: CoroutineScope,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IncludeLottieFile(
            draw = R.raw.network, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 8.dp)
        )
        Text(text = "Choisissez votre mode de connexion", style = MaterialTheme.typography.h4)
        Text(
            text = "Deux modes de connexion sont disponibles, le mode local et le mode distant. Le mode local permet de stocker les données sur votre appareil, le mode distant permet de stocker les données sur un serveur distant.",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Justify
        )
        Button(
            onClick = {
                SessionManager.setRepositoryType(RepositoryType.LOCAL)
                onFinish()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(height = 48.dp)
        ) {
            Text(text = "Utiliser le mode local", textAlign = TextAlign.Center)
        }
        Button(
            onClick = {
                SessionManager.setRepositoryType(RepositoryType.LOCAL)
                onFinish()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .height(height = 48.dp)
        ) {
            Text(text = "Utiliser le mode distant", textAlign = TextAlign.Center)
        }
    }
}