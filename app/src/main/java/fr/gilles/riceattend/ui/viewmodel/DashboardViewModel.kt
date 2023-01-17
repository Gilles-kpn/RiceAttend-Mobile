package fr.gilles.riceattend.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.models.Statistics
import fr.gilles.riceattend.services.api.ApiCallback
import fr.gilles.riceattend.services.api.ApiEndpoint
import fr.gilles.riceattend.services.api.ApiResponseError
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val apiEndpoint: ApiEndpoint
) : ViewModel() {
    var statistics by mutableStateOf<Statistics?>(null)
    var loading by mutableStateOf(false)


    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        loading = true
        viewModelScope.launch {
            apiEndpoint.statisticsRepository.statistics()
                .enqueue(object : ApiCallback<Statistics>() {
                    override fun onSuccess(response: Statistics) {
                        statistics = response

                        loading = false
                    }

                    override fun onError(error: ApiResponseError) {
                        loading = false
                    }
                }
                )
        }
    }
}