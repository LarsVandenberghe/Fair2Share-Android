package com.example.fair2share.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.R
import com.example.fair2share.Utils
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.network.ActivityApi
import com.example.fair2share.network.ProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class AddEditActivityFragmentViewModel(val activity: ActivityProperty): ViewModel() {
    private var viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val isNewActivity: Boolean
        get() = activity.activityId == null

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate

    fun createOrUpdate(){
        _coroutineScope.launch {
            try {
                val response = if (isNewActivity){
                    ActivityApi.retrofitService.addActivity(activity).await()
                } else {
                    ActivityApi.retrofitService.updateActivity(activity.activityId!!, activity).await()
                }
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