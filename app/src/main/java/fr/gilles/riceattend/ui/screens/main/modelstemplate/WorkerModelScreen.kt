package fr.gilles.riceattend.ui.screens.main.modelstemplate

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.app.SessionManager.Companion.context
import fr.gilles.riceattend.services.entities.models.Worker
import fr.gilles.riceattend.services.entities.models.WorkerActivity
import fr.gilles.riceattend.ui.screens.main.fragments.WorkerFormViewModel
import fr.gilles.riceattend.ui.widget.components.AppBar
import fr.gilles.riceattend.ui.widget.components.LoadingCard
import fr.gilles.riceattend.ui.widget.components.WorkerForm
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkerModelScreen(
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: WorkerViewModel = remember {
        WorkerViewModel(
            navHostController.previousBackStackEntry?.arguments?.getString(
                "code"
            ) ?: ""
        )
    }
) {
    val scope = rememberCoroutineScope()
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    when (viewModel.loading) {
        true -> {
            LoadingCard()
        }
        false -> {
            when (viewModel.worker) {
                null -> {}
                else -> {
                    viewModel.worker?.let {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AppBar(
                                title = it.name,
                                leftContent = {
                                    IconButton(
                                        onClick = {
                                            navHostController.popBackStack()
                                        },
                                    ) {
                                        Icon(
                                            Icons.Outlined.ArrowBack,
                                            "Back",
                                            tint = MaterialTheme.colors.background
                                        )
                                    }
                                },
                                rightContent = {
                                    IconButton(onClick = {}) {

                                    }
                                }
                            )

                            Card(
                                modifier = Modifier
                                    .padding(top = 30.dp, bottom = 20.dp)
                                    .width(100.dp)
                                    .height(100.dp)
                                    .clip(CircleShape)
                            ) {
                                Icon(Icons.Outlined.AccountCircle, "Worker image")
                            }
                            Text(
                                text = it.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 7.dp)
                            )
                            Text(
                                text = it.phone,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(bottom = 7.dp)
                                    .clickable { viewModel.openDialer() }
                            )
                            Text(
                                text = it.email,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .clickable { viewModel.openMail() },
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 20.dp,
                                        end = 20.dp,
                                        top = 10.dp,
                                        bottom = 10.dp
                                    )
                            ) {
                                IconButton(
                                    onClick = { /*TODO*/ }, modifier = Modifier
                                        .clip(CircleShape)
                                ) {
                                    Icon(Icons.Outlined.Assignment, "Add to activity")
                                }
                                IconButton(
                                    onClick = { scope.launch { modalBottomSheetState.show() } },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colors.background)
                                ) {
                                    Icon(
                                        Icons.Outlined.Edit,
                                        "Edit",
                                        tint = MaterialTheme.colors.secondary
                                    )
                                }

                                IconButton(
                                    onClick = { /*TODO*/ }, modifier = Modifier
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colors.background)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colors.error
                                    )
                                }


                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 15.dp),
                                Arrangement.SpaceBetween,
                                Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Activites",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = { /*TODO*/ }) {
                                    //filter
                                    Icon(Icons.Outlined.FilterList, "Filter")
                                }
                            }
                            Divider(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    when (viewModel.activities.isEmpty()) {

                                        true -> {
                                            Text(
                                                text = "Aucune activité",
                                                modifier = Modifier.padding(top = 20.dp)
                                            )
                                        }
                                        false -> {
                                            viewModel.activities.forEach { activity ->

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
    }

    ModalBottomSheetLayout(
        sheetContent = {
            WorkerForm(
                onSubmit = {
                    viewModel.updateWorker {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Une erreur est survenue\n$it",
                            )
                        }
                    }
                },
                title = "Modifier les informations",
                workerFormViewModel = viewModel.workerFormViewModel,
                buttonText = "Modifier",
                isLoading = viewModel.updateLoading
            )
        },
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {}
}


class WorkerViewModel(val code: String) : ViewModel() {
    var loading by mutableStateOf(false)
    var worker by mutableStateOf<Worker?>(null)
    var activities by mutableStateOf<List<WorkerActivity>>(listOf())
    var workerFormViewModel by mutableStateOf(WorkerFormViewModel())
    var updateLoading by mutableStateOf(false)

    init {
        getWorker()
    }

    fun openDialer() {
        worker?.let {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${it.phone}"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.get()?.startActivity(intent)
        }
    }

    fun openMail() {
        worker?.let {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${it.email}"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.get()?.startActivity(intent)
        }
    }

    private fun getWorker() {
        loading = true
        viewModelScope.launch {
            Log.d("WorkerViewModel", "getWorker $code")
            ApiEndpoint.workerRepository.get(code)
                .enqueue(object : ApiCallback<Worker>() {
                    override fun onSuccess(response: Worker) {
                        worker = response
                        loading = false
                        initUpdateFormViewModel()
                        loadWorkerActivities()
                        Log.d("WorkerViewModel", "Worker: ${response}")

                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                        Log.d("WorkerViewModel", error.message)
                    }

                })
        }
    }

    private fun initUpdateFormViewModel() {
        worker?.let {
            workerFormViewModel.firstNameState.value = it.firstName
            workerFormViewModel.lastNameState.value = it.lastName
            workerFormViewModel.phoneState.value = it.phone
            workerFormViewModel.emailState.value = it.email
            it.address.let { address ->
                workerFormViewModel.addressCityState.value =
                    if (address.city.isNullOrEmpty()) "" else address.city!!
                workerFormViewModel.addressStreetState.value =
                    if (address.street.isNullOrEmpty()) "" else address.street!!
                workerFormViewModel.addressCountryState.value =
                    if (address.country.isNullOrEmpty()) "" else address.country!!
            }
            workerFormViewModel.hourlyPayState.value = it.hourlyPay
        }
    }

    fun updateWorker(onError: (String) -> Unit) {
        updateLoading = true
        viewModelScope.launch {
            worker?.let {
                ApiEndpoint.workerRepository.update(
                    it.code,
                    workerFormViewModel.toWorkerPayload()
                ).enqueue(object : ApiCallback<Worker>() {
                    override fun onSuccess(response: Worker) {
                        worker = response
                        updateLoading = false
                        initUpdateFormViewModel()
                    }

                    override fun onError(error: ApiResponseError) {
                        updateLoading = false
                        Log.d("WorkerViewModel", error.message)
                    }
                })
            }
        }
    }

    private fun loadWorkerActivities() {
        worker?.let {
            loading = true
            viewModelScope.launch {
                ApiEndpoint.workerRepository.getWorkerActivities(it.code)
                    .enqueue(object : ApiCallback<List<WorkerActivity>>() {
                        override fun onSuccess(response: List<WorkerActivity>) {
                            activities = response
                            loading = false
                            Log.d("WorkerViewModel", "Activities: ${response}")
                        }

                        override fun onError(error: ApiResponseError) {
                            loading = false
                            Log.d("WorkerViewModel", error.message)
                        }

                    })
            }
        }
    }

}