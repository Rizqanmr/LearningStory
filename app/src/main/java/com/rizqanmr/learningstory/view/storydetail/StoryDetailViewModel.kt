package com.rizqanmr.learningstory.view.storydetail

import androidx.lifecycle.ViewModel
import com.rizqanmr.learningstory.data.repository.AppRepository
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class StoryDetailViewModel(private val repository: AppRepository) : ViewModel() {

    private var storyId: String = ""

    fun setStoryId(storyId: String) {
        this.storyId = storyId
    }

    fun getStoryDetail() = repository.getStoryDetail(storyId)

    fun getDate(date: String): String {
        var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return try {
            val formatter = simpleDateFormat.parse(date)
            simpleDateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            simpleDateFormat.format(formatter!!)
        } catch (e: ParseException) {
            e.printStackTrace()
            date
        }
    }
}