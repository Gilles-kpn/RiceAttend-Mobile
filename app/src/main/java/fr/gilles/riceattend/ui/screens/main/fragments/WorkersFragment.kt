package fr.gilles.riceattend.ui.screens.main.fragments

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.entities.models.*
import fr.gilles.riceattend.ui.formfields.EmailFieldState
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.regex.Pattern


@OptIn(ExperimentalMaterialApi::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun WorkersFragment(
    onMenuClick: () -> Unit = {},
    navHostController: NavHostController,
    scope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState,
    viewModel: WorkersViewModel = remember { WorkersViewModel() }
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var created by remember { mutableStateOf(false) }
    if (created) OpenDialog(
        title = "Ajouter un ouvrier",
        onDismiss = { created = false },
        onConfirm = { created = false; },
        show = created,
        content = { Text(text = "Ouvrier ajouté avec succès") })
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AppBar(title = "Ouvriers", leftContent = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Outlined.Menu, "Menu", tint = MaterialTheme.colors.background)
            }
        }, rightContent = {
            IconButton(onClick = { scope.launch { modalBottomSheetState.show() } }) {
                Icon(
                    Icons.Outlined.Add,
                    "More",
                    tint = MaterialTheme.colors.background
                )
            }
        })
        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                InputWidget(
                    state = viewModel.searchState,
                    title = "Rechercher",
                    trailing = {
                        IconButton(onClick = {
                        }) {
                            Icon(Icons.Outlined.Search, "Rechercher")
                        }
                    },
                    roundedCornerShape = RoundedCornerShape(30.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.FilterList, "Filtrer")
                    Text("Filtrer", modifier = Modifier.padding(10.dp))
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            when (viewModel.loading) {
                true -> {
                    LoadingCard()
                }
                false -> {
                    when (viewModel.workers) {
                        null -> {
                            LoadingCard()
                        }
                        else -> {
                            viewModel.workers?.let {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState()),
                                    verticalArrangement = if (it.empty == true) Arrangement.Center else Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    if (it.empty == true)
                                        Text(
                                            "Aucune ouvrier trouvé",
                                            style = MaterialTheme.typography.body1
                                        )
                                    else it.content.forEach { worker ->
                                        WorkerTile(worker, onClick = {
                                            navHostController.navigate(
                                                Route.WorkerRoute.path.replace(
                                                    "{code}",
                                                    worker.code
                                                )
                                            ) {}

                                        })
                                        Divider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    ModalBottomSheetLayout(
        sheetContent = {
            WorkerForm(
                workerFormViewModel = viewModel.workerFormViewModel,
                onSubmit = {
                    viewModel.create(
                        onSuccess = { created = true },
                        onError = { scope.launch { snackbarHostState.showSnackbar(it) } }
                    )
                }, isLoading = viewModel.creationLoading
            )
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {}
}

@RequiresApi(Build.VERSION_CODES.O)
class WorkersViewModel : ViewModel() {
    var searchState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { true },
            errorMessage = { "" },
        )
    )
    var params by mutableStateOf(Params())
    var workers by mutableStateOf<Page<Worker>?>(null)
    var loading by mutableStateOf(false)
    var workerFormViewModel by mutableStateOf(WorkerFormViewModel())
    var creationLoading by mutableStateOf(false)


    init {
        loadWorkers()
    }

    private fun loadWorkers() {
        loading = true

        viewModelScope.launch {
            Log.d("WorkersViewModel", "loadWorkers")
            ApiEndpoint.workerRepository.get(params.toMap())
                .enqueue(object : ApiCallback<Page<Worker>>() {
                    override fun onSuccess(response: Page<Worker>) {
                        workers = response
                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                        Log.d("WorkerViewModel", error.message)
                    }
                })
        }
    }

    fun create(onError: (String) -> Unit = {}, onSuccess: () -> Unit = {}) {
        creationLoading = true
        viewModelScope.launch {
            ApiEndpoint.workerRepository.create(workerFormViewModel.toWorkerPayload())
                .enqueue(object : ApiCallback<Worker>() {
                    override fun onSuccess(response: Worker) {
                        creationLoading = false
                        workers?.let {
                            it.content = it.content + (response)
                        }
                        onSuccess()
                    }

                    override fun onError(error: ApiResponseError) {
                        creationLoading = false
                        onError(error.message)
                        Log.d("WorkerViewModel", error.message)
                    }
                })
        }
    }
}


class WorkerFormViewModel {

    var firstNameState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { it.isNotBlank() },
            errorMessage = { "Nom requis" },
        )
    )
    var lastNameState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { it.isNotBlank() },
            errorMessage = { "Prénom requis" },
        )
    )
    var phoneState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = {
                Pattern.matches(
                    "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}\$",
                    it
                )
            },
            errorMessage = { "Téléphone requis" },
        )
    )
    var emailState by mutableStateOf(EmailFieldState())

    var hourlyPayState by mutableStateOf(
        TextFieldState(
            defaultValue = 1,
            validator = { it > 0 },
            errorMessage = { "Paye horaire requis superieur 1 FCFA" },
        )
    )

    var addressCountryState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { it.isNotBlank() },
            errorMessage = { "Pays requis" },
        )
    )

    var addressCityState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { it.isNotBlank() },
            errorMessage = { "Ville requise" },
        )
    )

    var addressStreetState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { it.isNotBlank() },
            errorMessage = { "Rue requise" },
        )
    )

    fun toWorkerPayload(): WorkerPayload {
        return WorkerPayload(
            name = firstNameState.value + " " + lastNameState.value,
            firstName = firstNameState.value,
            lastName = lastNameState.value,
            phone = phoneState.value,
            email = emailState.value,
            hourlyPay = hourlyPayState.value,
            address = Address(
                country = addressCountryState.value,
                city = addressCityState.value,
                street = addressStreetState.value
            )
        )
    }
}