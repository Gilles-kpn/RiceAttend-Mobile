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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.entities.models.*
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.ui.navigation.Route
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun PaddyFieldsFragment(
    onMenuClick: () -> Unit = {},
    viewModel: PaddyFieldsViewModel = remember { PaddyFieldsViewModel() },
    scope: CoroutineScope = rememberCoroutineScope(),
    navHostController: NavHostController,
    snackBarHostState: SnackbarHostState,
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var created by remember { mutableStateOf(false) }
    if (created)
        OpenDialog(
            title = "Riziere creee",
            onDismiss = { created = false },
            onConfirm = { created = false; },
            show = created,
            content = {
                Text("Riziere creee avec succes")
            }
        )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AppBar(title = "Rizieres", leftContent = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Outlined.Menu, "Menu", tint = MaterialTheme.colors.background)
            }
        }, rightContent = {
            IconButton(onClick = { scope.launch { modalBottomSheetState.show() } }) {
                Icon(Icons.Outlined.Add, "Add", tint = Color.White)
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
                    viewModel.paddyFields?.let {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = if (it.empty == true) Arrangement.Center else Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            if (it.empty == true)
                                Text(
                                    "Aucune riziere trouvé",
                                    style = MaterialTheme.typography.body1
                                )
                            else {

                                it.content.forEach { paddyField ->
                                    PaddyFieldTile(paddyField, onClick = {
                                        navHostController.navigate(
                                            Route.PaddyFieldRoute.path.replace(
                                                "{code}",
                                                paddyField.code
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
    ModalBottomSheetLayout(
        sheetContent = {
            PaddyFieldForm(
                title = "Créer une rizière",
                paddyFormViewModel = viewModel.paddyFormViewModel,
                plants = viewModel.plants,
                onClick = {
                    viewModel.createPaddyField(
                        onError = {
                            scope.launch {
                                snackBarHostState.showSnackbar(it)
                            }
                        },
                        onSuccess = { created = true }
                    )
                },
                isLoading = viewModel.paddyFieldCreationLoading,
                buttonText = "Enregistrer",

                )
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {}

}


@RequiresApi(Build.VERSION_CODES.O)
class PaddyFieldsViewModel : ViewModel() {
    val searchState by mutableStateOf(TextFieldState(validator = {
        it.isNotBlank() && it.isNotEmpty()
    }, errorMessage = {
        "Valeur a rechercher requis"
    }, defaultValue = ""))
    var paddyFields: Page<PaddyField>? by mutableStateOf(null)
    var plants: Page<Plant>? by mutableStateOf(null)
    var queryParams: Params = Params()
    var loading by mutableStateOf(false)
    val paddyFormViewModel by mutableStateOf(PaddyFieldFormViewModel())
    var paddyFieldCreationLoading by mutableStateOf(false)

    init {
        loadPaddyField()
        loadPlants()
    }


    private fun loadPaddyField() {
        loading = true
        viewModelScope.launch {
            ApiEndpoint.paddyFieldRepository.get(queryParams.toMap())
                .enqueue(object : ApiCallback<Page<PaddyField>>() {
                    override fun onSuccess(response: Page<PaddyField>) {
                        paddyFields = response
                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                        Log.d("PaddyFieldViewModel", "Error: ${error.message}")
                    }
                })
        }
    }

    private fun loadPlants() {
        viewModelScope.launch {
            ApiEndpoint.resourceRepository.getPlant(
                Params(
                    pageNumber = 0,
                    pageSize = 200,
                    sort = Sort.DESC
                ).toMap()
            )
                .enqueue(object : ApiCallback<Page<Plant>>() {
                    override fun onSuccess(response: Page<Plant>) {
                        plants = response
                    }

                    override fun onError(error: ApiResponseError) {
                        Log.d("PaddyFieldViewModel", "Error: ${error.message}")
                    }
                })
        }
    }

    fun createPaddyField(onError: (String) -> Unit = {}, onSuccess: () -> Unit = {}) {
        paddyFieldCreationLoading = true
        viewModelScope.launch {
            ApiEndpoint.paddyFieldRepository.create(paddyFormViewModel.toPaddyFieldPayload())
                .enqueue(object : ApiCallback<PaddyField>() {
                    override fun onSuccess(response: PaddyField) {
                        paddyFieldCreationLoading = false
                        onSuccess()
                        paddyFields?.let {
                            it.content = it.content + response
                        }
                    }

                    override fun onError(error: ApiResponseError) {
                        paddyFieldCreationLoading = false
                        onError(error.message)
                        Log.d("PaddyFieldViewModel", "Error: ${error.message}")
                    }
                })
        }
    }

}

class PaddyFieldFormViewModel {
    var name by mutableStateOf(TextFieldState(validator = {
        it.isNotBlank() && it.isNotEmpty()
    }, errorMessage = {
        "Nom requis"
    }, defaultValue = ""))

    //same thing with description, address_country, address_city, address_street, numberOfPlants, surface_value,surface_unit
    var description by mutableStateOf(TextFieldState(validator = {
        it.isNotBlank() && it.isNotEmpty()
    }, errorMessage = {
        "Description requise"
    }, defaultValue = ""))
    var plant by mutableStateOf(TextFieldState(validator = {
        it.code.isBlank().not()
    }, errorMessage = {
        "Plant requise"
    }, defaultValue = Plant(
        name = "",
        description = "",
        image = "",
        color = "",
        shape = ""
    )
    )
    )
    var numberOfPlants by mutableStateOf(TextFieldState(validator = {
        it >= 1
    }, errorMessage = {
        "Nombre de plantes requis"
    }, defaultValue = 1))
    var surface_value by mutableStateOf(TextFieldState<Int>(validator = {
        it >= 1
    }, errorMessage = {
        "Surface requise et doit être supérieur à 1"
    },
        defaultValue = 1
    )
    )
    var surface_unit by mutableStateOf(TextFieldState<String>(validator = {
        it.isNotBlank() && it.isNotEmpty()
    }, errorMessage = {
        "Unité de surface requise"
    },
        defaultValue = "m²"
    )
    )


    fun toPaddyFieldPayload(): PaddyFielPayLoad {
        return PaddyFielPayLoad(
            name = name.value,
            description = description.value,
            plantCode = plant.value.code,
            numberOfPlants = numberOfPlants.value,
            surface = Surface(
                value = surface_value.value.toLong(),
                unit = surface_unit.value
            )
        )
    }

}





