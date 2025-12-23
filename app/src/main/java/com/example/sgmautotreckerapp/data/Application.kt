package com.example.sgmautotreckerapp.data

import android.app.Application
import com.example.sgmautotreckerapp.data.database.CarDatabase
import com.example.sgmautotreckerapp.data.sync.SyncRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@HiltAndroidApp
class CarApplication : Application() {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        val db = CarDatabase.getDatabase(this)
        val syncRepository = SyncRepository(db)

        appScope.launch {
            try {
                syncRepository.exportToServer()
            } catch (_: Exception) {
            }

            try {
                syncRepository.importFromServer()
            } catch (_: Exception) {
            }

            while (isActive) {
                try {
                    syncRepository.exportToServer()
                    syncRepository.importFromServer()
                } catch (_: Exception) {
                }
                delay(60_000L)
            }
        }
    }
}
