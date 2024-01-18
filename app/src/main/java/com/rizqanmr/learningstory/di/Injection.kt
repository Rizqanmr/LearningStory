package com.rizqanmr.learningstory.di

import android.content.Context
import com.rizqanmr.learningstory.data.repository.AppRepository
import com.rizqanmr.learningstory.data.api.ApiConfig
import com.rizqanmr.learningstory.data.pref.UserPreference
import com.rizqanmr.learningstory.data.pref.dataStore
import com.rizqanmr.learningstory.database.StoryDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): AppRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val database = StoryDatabase.getDatabase(context)
        return AppRepository.getInstance(pref, apiService, database)
    }
}