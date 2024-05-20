package miso4203.mobile.app.vinilos.repositories

import android.app.Application
import miso4203.mobile.app.vinilos.database.dao.CollectorDao
import miso4203.mobile.app.vinilos.models.Collector
import miso4203.mobile.app.vinilos.network.NetworkServiceAdapter

class CollectorRepository(
    private val application: Application,
    private val collectorDao: CollectorDao,
    ) {
    suspend fun refreshData(): List<Collector> {
        return if (NetworkServiceAdapter.isInternetAvailable(application.applicationContext)) {
            val collectors = NetworkServiceAdapter.getInstance(application).getCollectors()
            this.collectorDao.insertManyRaw(collectors)

            collectors
        } else {
            val cached = this.collectorDao.getAll()

            if (cached.isNullOrEmpty()) {
                emptyList()
            } else cached
        }
    }

}