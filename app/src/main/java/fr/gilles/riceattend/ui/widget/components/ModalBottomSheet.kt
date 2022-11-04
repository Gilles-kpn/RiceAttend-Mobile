package fr.gilles.riceattend.ui.widget.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import fr.gilles.riceattend.services.entities.models.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddPaddyFieldModalBottomSheet(
    viewModel: AddPaddyFieldViewModel ,
    modalBottomSheetState: ModalBottomSheetState
) {
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            when (viewModel.loading) {
                true -> LoadingCard()
                false -> {
                    viewModel.paddyFields?.let {
                        LazyColumn(Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 10.dp)) {
                            item {
                                Text(
                                    text = "Choisissez les rizieres a ajouter",
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            items(it.content.size) { itemIndex ->
                                Row(
                                    Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    var alreadyExist by remember { mutableStateOf(false) }
                                    alreadyExist =
                                        viewModel.alreadyExists.stream().anyMatch { inAlready ->
                                            inAlready.paddyField.code == it.content[itemIndex].code
                                        }
                                    Checkbox(
                                        checked = if (alreadyExist) alreadyExist else viewModel.toBeAdded.contains(
                                            it.content[itemIndex]
                                        ),
                                        enabled = !alreadyExist,
                                        onCheckedChange = { checkedChange ->
                                            if(!alreadyExist){
                                                if (checkedChange)
                                                    viewModel.toBeAdded += it.content[itemIndex]
                                                else
                                                    viewModel.toBeAdded -= it.content[itemIndex]
                                            }
                                        }
                                    )
                                    PaddyFieldTile(paddyField = it.content[itemIndex])
                                }
                            }
                            item {
                                Button(
                                    onClick = { viewModel.add() },
                                    enabled = viewModel.toBeAdded.isNotEmpty() && !viewModel.inAdded,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                ) {
                                    if (viewModel.inAdded) CircularProgressIndicator()
                                    else Text(
                                        "Ajouter",
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp,
                                            vertical = 5.dp
                                        )
                                    )
                                }
                            }
                        }

                    }
                }
            }
        },
    ) {}

}

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class AddPaddyFieldViewModel @AssistedInject constructor(
    @Assisted val alreadyExists: List<ActivityPaddyFieldWithoutActivity>,
    @Assisted val onAddPaddyFields: (List<ActivityPaddyFieldWithoutActivity>) -> Unit = {},
    @Assisted val activityCode:String,
    private val apiEndpoint: ApiEndpoint
) : ViewModel() {
    var loading by mutableStateOf(false)
    var params by mutableStateOf(Params())
    var paddyFields by mutableStateOf<Page<PaddyField>?>(null)
    var toBeAdded by mutableStateOf<List<PaddyField>>(listOf())
    var inAdded by mutableStateOf(false)


    init {
        loadPaddyFields()
    }


    private fun loadPaddyFields() {
        loading = true
        viewModelScope.launch {
            apiEndpoint.paddyFieldRepository.get(params.toMap())
                .enqueue(object : ApiCallback<Page<PaddyField>>() {
                    override fun onSuccess(response: Page<PaddyField>) {
                        if(paddyFields == null) paddyFields = response
                        else {
                            val temp = paddyFields!!.content
                            paddyFields = response
                            paddyFields?.content = paddyFields?.content?.plus(temp)!!
                        }
                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                    }

                })
        }
    }

    fun add(){
        inAdded = true
        viewModelScope.launch {
            apiEndpoint.activityRepository.addPaddyFieldsToActivity(
                activityCode,
                toBeAdded.map { it.code }
            ).enqueue(object: ApiCallback<List<ActivityPaddyFieldWithoutActivity>>(){
                override fun onSuccess(response: List<ActivityPaddyFieldWithoutActivity>) {
                    onAddPaddyFields(response)
                    inAdded = false
                }

                override fun onError(error: ApiResponseError) {
                    inAdded = false
                }

            })
        }
    }

    @AssistedFactory
    interface Factory{
        fun create(code: String,  alreadyExists: List<ActivityPaddyFieldWithoutActivity>, onAddPaddyFields: (List<ActivityPaddyFieldWithoutActivity>) -> Unit ): AddPaddyFieldViewModel
    }



    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            addPaddyFieldVM: Factory,
            code: String,
            alreadyExists: List<ActivityPaddyFieldWithoutActivity>, onAddPaddyFields: (List<ActivityPaddyFieldWithoutActivity>) -> Unit
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return addPaddyFieldVM.create(code,alreadyExists, onAddPaddyFields) as T
            }
        }
    }


}





@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddWorkersModalBottomSheet(
    viewModel: AddWorkersViewModel  ,
    modalBottomSheetState: ModalBottomSheetState
) {
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            when (viewModel.loading) {
                true -> LoadingCard()
                false -> {
                    viewModel.workers?.let {
                        LazyColumn(Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 10.dp)) {
                            item {
                                Text(
                                    text = "Choisissez les ouvriers a ajouter",
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            items(it.content.size) { itemIndex ->
                                Row(
                                    Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    var alreadyExist by remember { mutableStateOf(false) }
                                    alreadyExist =
                                        viewModel.alreadyExists.stream().anyMatch { inAlready ->
                                            inAlready.worker.code == it.content[itemIndex].code
                                        }
                                    Checkbox(
                                        checked = if (alreadyExist) alreadyExist else viewModel.toBeAdded.contains(
                                            it.content[itemIndex]
                                        ),
                                        enabled = !alreadyExist,
                                        onCheckedChange = { checkedChange ->
                                            if(!alreadyExist){
                                                if (checkedChange)
                                                    viewModel.toBeAdded += it.content[itemIndex]
                                                else
                                                    viewModel.toBeAdded -= it.content[itemIndex]
                                            }
                                        }
                                    )
                                    WorkerTile(worker = it.content[itemIndex])
                                }
                            }
                            item {
                                Button(
                                    onClick = { viewModel.add() },
                                    enabled = viewModel.toBeAdded.isNotEmpty() && !viewModel.inAdded,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp)
                                ) {
                                    if (viewModel.inAdded) CircularProgressIndicator()
                                    else Text(
                                        "Ajouter",
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp,
                                            vertical = 5.dp
                                        )
                                    )
                                }
                            }
                        }

                    }
                }
            }
        },
    ) {

    }

}

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class AddWorkersViewModel @AssistedInject constructor(
    @Assisted val alreadyExists: List<ActivityWorkerWithoutActivity>,
    @Assisted val onAddWorkersToActivity: (List<ActivityWorkerWithoutActivity>) -> Unit = {},
    @Assisted val activityCode:String,
    private val  apiEndpoint: ApiEndpoint
) : ViewModel() {
    var loading by mutableStateOf(false)
    var params by mutableStateOf(Params())
    var workers by mutableStateOf<Page<Worker>?>(null)
    var toBeAdded by mutableStateOf<List<Worker>>(listOf())
    var inAdded by mutableStateOf(false)


    init {
        loadWorkers()
    }


    private fun loadWorkers() {
        loading = true
        viewModelScope.launch {
            apiEndpoint.workerRepository.get(params.toMap())
                .enqueue(object : ApiCallback<Page<Worker>>() {
                    override fun onSuccess(response: Page<Worker>) {
                        if(workers == null) workers = response
                        else {
                            val temp = workers!!.content
                            workers = response
                            workers?.content = workers?.content?.plus(temp)!!
                        }
                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                    }

                })
        }
    }

    fun add(){
        inAdded = true
        viewModelScope.launch {
            apiEndpoint.activityRepository.addWorkersToActivity(
                activityCode,
                toBeAdded.map { it.code }
            ).enqueue(object: ApiCallback<List<ActivityWorkerWithoutActivity>>(){
                override fun onSuccess(response: List<ActivityWorkerWithoutActivity>) {
                    onAddWorkersToActivity(response)
                    inAdded = false
                }

                override fun onError(error: ApiResponseError) {
                    inAdded = false
                }

            })
        }
    }


    @AssistedFactory
    interface Factory{
        fun create(activityCode: String,
                   alreadyExists: List<ActivityWorkerWithoutActivity>,
                   onAddPaddyFields: (List<ActivityWorkerWithoutActivity>) -> Unit): AddWorkersViewModel
    }



    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            addWorkerVM: Factory,
            alreadyExists: List<ActivityWorkerWithoutActivity>,
            onAddWorker: (List<ActivityWorkerWithoutActivity>) -> Unit,
            code: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return addWorkerVM.create(code, alreadyExists,onAddWorker) as T
            }
        }
    }

}