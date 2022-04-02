package com.example.redmad.ui

import android.app.Application
import com.example.redmad.data.api.AppService
import com.example.redmad.data.repository.AppRepository
import com.example.redmad.data.source.RemoteDataSource

class MainApplication : Application() {

    lateinit var appRepository: AppRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val appService = RemoteDataSource.getInstance().create(AppService::class.java)
        appRepository = AppRepository(appService, applicationContext)
    }

}