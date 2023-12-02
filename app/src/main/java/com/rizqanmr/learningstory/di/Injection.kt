package com.rizqanmr.learningstory.di

import android.content.Context
import com.rizqanmr.learningstory.data.UserRepository
import com.rizqanmr.learningstory.data.pref.UserPreference
import com.rizqanmr.learningstory.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}