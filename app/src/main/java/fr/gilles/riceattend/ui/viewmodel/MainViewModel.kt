package fr.gilles.riceattend.ui.viewmodel

import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.gilles.riceattend.R
import fr.gilles.riceattend.app.RiceAttendApp
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: RiceAttendApp
) : AndroidViewModel(application) {

    fun test() {
        getApplication<RiceAttendApp>().getString(R.string.app_name)
    }
}
