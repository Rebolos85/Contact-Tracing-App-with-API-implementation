package com.example.trazeapp.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trazeapp.repository.source.AuthSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val auth: AuthSource
) : ViewModel() {
    private val _bottomNavigationVisibility = MutableLiveData<Int>()

    val bottomNavigationVisibility: LiveData<Int>
        get() = _bottomNavigationVisibility


    fun showBottomNavigation() {
        _bottomNavigationVisibility.postValue(View.VISIBLE)

    }

    fun hideBottomNavigation() {
        _bottomNavigationVisibility.postValue(View.GONE)
    }
    fun hasUserAuthenticated(): Boolean = auth.hasUser()
    fun hasUserState(): Flow<Boolean> = auth.hasUserFlow()
}