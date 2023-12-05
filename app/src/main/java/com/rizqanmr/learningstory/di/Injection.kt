package com.rizqanmr.learningstory.di

import android.content.Context
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.api.ApiConfig
import com.rizqanmr.learningstory.data.pref.UserPreference
import com.rizqanmr.learningstory.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): AppRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(context)
        return AppRepository.getInstance(pref, apiService)
    }
}