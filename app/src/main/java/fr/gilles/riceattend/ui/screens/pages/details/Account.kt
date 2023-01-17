package fr.gilles.riceattend.ui.screens.pages.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import fr.gilles.riceattend.services.storage.SessionManager
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.screens.pages.Drawer
import fr.gilles.riceattend.ui.viewmodel.AccountVM
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountScreen(
    snackbarHostState: SnackbarHostState,
    navHostController: NavHostController,
    accountVM: AccountVM = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val optionsModalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val changePasswordModalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = { Drawer(navHostController, scope, scaffoldState) },
        topBar = {
            AppBar(title = "Mon Compte", leftContent = {
                IconButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(Icons.Outlined.Menu, "Menu",)
                }
            }, rightContent = {
                IconButton(onClick = {
                    scope.launch {
                        optionsModalBottomSheetState.show()
                    }
                }) {
                    Icon(Icons.Outlined.MoreVert, "options")
                }
            })
        },
        drawerGesturesEnabled = true,
        content = { padding ->
            LazyColumn(Modifier.padding(padding)) {
                SessionManager.session.user?.let {
                    item {
                        Card(
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    modifier = Modifier
                                        .padding(end = 20.dp, start = 10.dp)
                                        .width(100.dp)
                                        .height(100.dp)
                                        .clip(CircleShape)
                                ) {
                                    AsyncImage(
                                        model = userGravatar(email = it.email),
                                        contentDescription = "Profile picture",
                                        contentScale = ContentScale.FillWidth,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                }
                                Column {
                                    Text(
                                        text = it.name,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 7.dp)
                                    )
                                    Text(
                                        text = it.email,
                                        modifier = Modifier
                                            .padding(bottom = 10.dp)
                                    )
                                    Text(
                                        text = "Enregistré le " + formatDate(it.createdAt),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(bottom = 7.dp)
                                    )


                                }

                            }
                        }
                    }
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Notifications,
                                "notification",
                                Modifier.padding(vertical = 10.dp)
                            )
                            Text(text = "Mon historique ")
                        }
                    }
                    item {
                        var expand by remember {
                            mutableStateOf(false)
                        }
                        Card(
                            Modifier
                                .padding(horizontal = 15.dp, vertical = 7.dp)
                                .fillMaxWidth()
                                .clickable { expand = !expand }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                ) {
                                    Icon(
                                        Icons.Outlined.CalendarToday,
                                        "activity",
                                        Modifier.padding(horizontal = 10.dp)
                                    )
                                    Text(text = "Activités")
                                }
                                Icon(
                                    if (expand) Icons.Outlined.ExpandMore else Icons.Outlined.ChevronRight,
                                    contentDescription = "expend more"
                                )
                            }
                        }
                    }
                    item {
                        var expand by remember {
                            mutableStateOf(false)
                        }
                        Card(
                            Modifier
                                .padding(horizontal = 15.dp, vertical = 7.dp)
                                .fillMaxWidth()
                                .clickable { expand = !expand }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                ) {
                                    Icon(
                                        Icons.Outlined.Landscape,
                                        "paddyfield",
                                        Modifier.padding(horizontal = 10.dp)
                                    )
                                    Text(text = "Activités")
                                }
                                Icon(
                                    if (expand) Icons.Outlined.ExpandMore else Icons.Outlined.ChevronRight,
                                    contentDescription = "expend more"
                                )
                            }
                        }
                    }
                    item {
                        var expand by remember {
                            mutableStateOf(false)
                        }
                        Card(
                            Modifier
                                .padding(horizontal = 15.dp, vertical = 7.dp)
                                .fillMaxWidth()
                                .clickable { expand = !expand }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                ) {
                                    Icon(
                                        Icons.Outlined.People,
                                        "paddy fields",
                                        Modifier.padding(horizontal = 10.dp)
                                    )
                                    Text(text = "Ouvriers")
                                }
                                Icon(
                                    if (expand) Icons.Outlined.ExpandMore else Icons.Outlined.ChevronRight,
                                    contentDescription = "expend more"
                                )
                            }
                        }
                    }
                    item {
                        var expand by remember {
                            mutableStateOf(false)
                        }
                        Card(
                            Modifier
                                .padding(horizontal = 15.dp, vertical = 7.dp)
                                .fillMaxWidth()
                                .clickable { expand = !expand }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                ) {
                                    Icon(
                                        Icons.Outlined.Build,
                                        "workers",
                                        Modifier.padding(horizontal = 10.dp)
                                    )
                                    Text(text = "Ressources")
                                }
                                Icon(
                                    if (expand) Icons.Outlined.ExpandMore else Icons.Outlined.ChevronRight,
                                    contentDescription = "expend more"
                                )
                            }
                        }
                    }
                }

            }
        }
    )

    ModalBottomSheetLayout(
        sheetState = optionsModalBottomSheetState,
        sheetContent = {
            Column(Modifier.padding(10.dp)) {
                Text(text = "Actions")
                Row(Modifier.padding(10.dp).clickable {
                    scope.launch {
                        optionsModalBottomSheetState.hide()
                        changePasswordModalBottomSheetState.show()
                    }
                }, verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Lock,
                        contentDescription = "change password",
                        Modifier.padding(horizontal = 10.dp)
                    )
                    Text(text = "Modifier mon mot de passe")
                }
                Row(
                    Modifier
                        .padding(10.dp)
                        .clickable {
                            SessionManager.clear()
                            navHostController.navigate(Route.LoginRoute.path) {
                                popUpTo(Route.MainRoute.path) {
                                    inclusive = true
                                }
                            }
                        }, verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Logout,
                        contentDescription = "logout",
                        Modifier.padding(horizontal = 10.dp)
                    )
                    Text(text = "Me deconnecter")
                }
            }
        }
    ) {

    }
    var passwordError by remember { mutableStateOf(false) }
    OpenDialog(
        title = "Attention",
        content = {
            Text(
                "Une erreur est survenue lors de la modification du mot de passe ",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(10.dp)
            )
        },
        onConfirm = {
            passwordError = false
        },
        onDismiss = {
            passwordError = false
        },
        show = passwordError,
        isSuccess = false
    )
    ModalBottomSheetLayout(
        sheetState = changePasswordModalBottomSheetState,
        sheetContent = {
            var passwordVisible by remember {
                mutableStateOf(false)
            }
            Column(Modifier.padding(10.dp)) {
                Text(text = "Modifier mon mot de passe")
                InputWidget(
                    state = accountVM.oldPasswordState,
                    title = "Ancien mot de passe",
                    icon = Icons.Outlined.Password,
                    singleLine = true,
                    keyboardType = KeyboardType.Password,
                    visualTransformation =  PasswordVisualTransformation(),
                )
                InputWidget(
                    state = accountVM.passwordState,
                    title = "Nouveau mot de passe",
                    icon = Icons.Outlined.Password,
                    trailing = {
                        IconButton(onClick = {
                            passwordVisible = !passwordVisible
                        }) {
                            if (passwordVisible)
                                Icon(
                                    Icons.Default.Visibility,
                                    contentDescription = "Password Visible"
                                )
                            else
                                Icon(
                                    Icons.Default.VisibilityOff,
                                    contentDescription = "Password Not Visible"
                                )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardType = if (passwordVisible) KeyboardType.Password else KeyboardType.Text
                )
                Button(
                    enabled = accountVM.oldPasswordState.isValid() && accountVM.passwordState.isValid(),
                    onClick = {
                        accountVM.changePassword(
                            callback = {
                                scope.launch {
                                    changePasswordModalBottomSheetState.hide()
                                    snackbarHostState.showSnackbar("Mot de passe modifié avec succes")
                                }
                            },
                            fallback = {
                                passwordError = true
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    if (accountVM.passwordChangeLoading)
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    else
                        Text(text = "Changer mon mot de passe")
                }
            }

        }
    ) {

    }


}