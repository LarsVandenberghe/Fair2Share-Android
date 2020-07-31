package com.example.fair2share.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.Utils
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.network.ActivityApi
import com.example.fair2share.network.ProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CreateActivityFragmentViewModel: ViewModel() {
    private var viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate

    fun create(activityProperty: ActivityProperty){
        _coroutineScope.launch {
            try {
                val response = ActivityApi.retrofitService.addActivity(activityProperty).await()
                Utils.throwExceptionIfHttpNotSuccessful(response)
                _navigate.value = true
            } catch (e:HttpException){
                _errorMessage.value = Utils.formExceptionsToString(e)
            } catch (t: Throwable){
                _errorMessage.value = t.message
            }
        }
    }
}