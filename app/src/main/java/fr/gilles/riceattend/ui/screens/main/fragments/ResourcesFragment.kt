package fr.gilles.riceattend.ui.screens.main.fragments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.entities.models.*
import fr.gilles.riceattend.ui.formfields.TextFieldState
import fr.gilles.riceattend.ui.widget.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview(showBackground = true)
fun ResourcesFragment(
    onMenuClick: () -> Unit = {},
    scope: CoroutineScope = rememberCoroutineScope(),
    viewModel: ResourcesViewModel = remember { ResourcesViewModel() }
) {
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var created by remember { mutableStateOf(false) }
    if (created) OpenDialog(title = "Ajouter une ressources",
        onDismiss = { created = false },
        onConfirm = { created = false; },
        show = created,
        content = { Text(text = "Nouvelle ressource enregistre avec success") })

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AppBar(title = "Ressources", leftContent = {
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
                .verticalScroll(rememberScrollState())
        ) {
            when (viewModel.loading) {
                true -> {
                    LoadingCard()
                }
                false -> {
                    when (viewModel.resources) {
                        null -> {
                            LoadingCard()
                        }
                        else -> {
                            viewModel.resources?.let {
                                if (it.empty == true)
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "Aucune ressource trouvée")
                                    }
                                else {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        it.content.forEach { resource ->
                                            ResourceTile(resource = resource, onClick = {})
                                            Divider()
                                        }
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {
                val types = listOf(
                    ResourceType.WATER,
                    ResourceType.OTHER,
                    ResourceType.FERTILIZER,
                    ResourceType.MATERIALS
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Agriculture, "Resource")
                    Text(text = "Creer une ressource")
                }
                InputWidget(state = viewModel.resourceFormViewModel.name, title = "Nom")
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        InputNumberWidget(
                            state = viewModel.resourceFormViewModel.quantity,
                            title = "Quantite",
                            icon = Icons.Outlined.Countertops
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        InputNumberWidget(
                            state = viewModel.resourceFormViewModel.price,
                            title = "Prix Unitaire",
                            icon = Icons.Outlined.PriceChange
                        )
                    }
                }
                InputDropDownSelect(
                    state = viewModel.resourceFormViewModel.type,
                    list = types,
                    template = {
                        Text(text = it.name)
                    },
                    title = "Type de ressource"
                )
                Button(
                    enabled = !viewModel.resourceFormViewModel.loading && (
                            viewModel.resourceFormViewModel.name.isValid() &&
                                    viewModel.resourceFormViewModel.quantity.isValid() &&
                                    viewModel.resourceFormViewModel.type.isValid()
                            ),
                    onClick = {
                        viewModel.create({ created = true }, {
                            scope.launch {

                            }
                        })
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(50.dp)
                ) {
                    if (viewModel.resourceFormViewModel.loading) {
                        CircularProgressIndicator()
                    } else {
                        Text("Enregistrer", style = MaterialTheme.typography.button)
                    }
                }
            }

        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {}

}

class ResourcesViewModel : ViewModel() {
    var resources by mutableStateOf<Page<Resource>?>(null)
    var params by mutableStateOf(Params())
    var loading by mutableStateOf(false)
    var searchState by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { true },
            errorMessage = { "" },
        )
    )
    val resourceFormViewModel by mutableStateOf(ResourceFormViewModel())


    init {
        loadResources()
    }

    private fun loadResources() {
        loading = true
        viewModelScope.launch {
            ApiEndpoint.resourceRepository.get(params.toMap())
                .enqueue(object : ApiCallback<Page<Resource>>() {
                    override fun onSuccess(response: Page<Resource>) {
                        resources = response
                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                    }
                })
        }

    }

    fun create(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        resourceFormViewModel.loading = true
        viewModelScope.launch {
            ApiEndpoint.resourceRepository.create(resourceFormViewModel.toResourcePayload())
                .enqueue(object : ApiCallback<Resource>() {
                    override fun onSuccess(response: Resource) {
                        resources?.let {
                            it.content = it.content + response
                        }
                        resourceFormViewModel.loading = false
                        onSuccess()
                    }

                    override fun onError(error: ApiResponseError) {
                        resourceFormViewModel.loading = false
                        onError(error.message)
                    }
                })
        }
    }
}

class ResourceFormViewModel {
    var loading by mutableStateOf(false)
    var name by mutableStateOf(
        TextFieldState(
            defaultValue = "",
            validator = { it.isNotBlank() },
            errorMessage = { "Nom requis" },
        )
    )

    var quantity by mutableStateOf(
        TextFieldState(
            defaultValue = 1,
            validator = { it > 0 },
            errorMessage = { "Quantie requise superieur a 1" },
        )
    )

    var price by mutableStateOf(
        TextFieldState(
            defaultValue = 1,
            validator = { it > 0 },
            errorMessage = { "Quantie requise superieur a 1" },
        )
    )

    var type by mutableStateOf(
        TextFieldState(
            defaultValue = ResourceType.WATER,
            validator = { it.name.isNotBlank() },
            errorMessage = { "Choisissez un type valide" },
        )
    )


    fun toResourcePayload(): ResourcePayload {
        return ResourcePayload(
            unitPrice = price.value.toLong(),
            name = name.value,
            resourceType = type.value,
            quantity = quantity.value.toLong()
        )
    }


}

