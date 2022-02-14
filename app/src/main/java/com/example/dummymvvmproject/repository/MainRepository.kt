package com.example.dummymvvmproject.repository

import android.util.Log
import com.codingwithmitch.daggerhiltplayground.util.DataState
import com.example.dummymvvmproject.data.local.datastore.AppPrefsStorage
import com.example.dummymvvmproject.data.local.room.CacheMapperPrayer
import com.example.dummymvvmproject.data.local.room.OldCahcerToNewModelMappper
import com.example.dummymvvmproject.data.local.room.PrayerCacheEntity
import com.example.dummymvvmproject.data.local.room.PrayerDao
import com.example.dummymvvmproject.data.remote.retrofit.PrayerRetrofit
import com.example.dummymvvmproject.model.PrayerData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainRepository
constructor(
    private val prayerDao: PrayerDao,
    private val prayerRetrofit: PrayerRetrofit,
    private val cacheMapperPrayer: CacheMapperPrayer,
    private val oldCahcerToNewModelMappper: OldCahcerToNewModelMappper,
    private val networkMapperRetrofit: com.example.dummymvvmproject.data.remote.retrofit.NetworkMapperRetrofit,
    val prefsStorage: AppPrefsStorage


) {
    companion object {
        val TAG = "MainRepositoryTAG"
    }

    suspend fun getBlogsTest(lat: String, lng: String): Flow<DataState<List<PrayerData>>> = flow {

        try {
            val oldCacheEntity = prayerDao.get()
            if (oldCacheEntity.isNullOrEmpty()) {
                emit(DataState.Loading)

                val dateFormat: DateFormat = SimpleDateFormat("MM")
                val date = Date()
                Log.d("Month", dateFormat.format(date))
                val dataNetworkEntity =
                    prayerRetrofit.get1(lat, lng, dateFormat.format(date), "2022", 1)
                val prayerData =
                    networkMapperRetrofit.mapFromEntityList(dataNetworkEntity.data)
                prayerData.map { prayerDao.insert(cacheMapperPrayer.mapToEntity(it)) }
                val cacheEntity = prayerDao.get()
                emit(DataState.General(cacheMapperPrayer.mapFromEntityList(cacheEntity)))
            } else {
                val cacheEntity = prayerDao.get()
                emit(DataState.Success(cacheMapperPrayer.mapFromEntityList(cacheEntity)))
            }

        } catch (e: Exception) {
            Log.e(TAG, "getBlogsTest: $e")
            emit(DataState.Success(cacheMapperPrayer.mapFromEntityList(getCachedValue())))
            emit(DataState.Error(e))

        }
    }
    /*  suspend fun getBlogsTest(lat: String, lng: String): Flow<DataState<List<PrayerData>>> = flow {
          emit(DataState.Loading)
          try {
              val dataNetworkEntity =
                  prayerRetrofit.get1(lat, lng, "2", "2022")
              val prayerData =
                  networkMapperRetrofit.mapFromEntityList(dataNetworkEntity.data)
              val oldCacheEntity = prayerDao.get()
              if (oldCacheEntity.isNullOrEmpty()) {
                  ///--here i have api data in prayerdata object
                  Log.d(TAG, "getBlogsTest: Is null or emtpy")
                  prayerData.map { prayerDao.insert(cacheMapperPrayer.mapToEntity(it)) }
              } else {

                  Log.d(TAG, "getBlogsTest: ${prayerData.size}")
                  Log.d(TAG, "getBlogsTest: ${oldCacheEntity.size}")

                  val mixedDataList =
                      oldCahcerToNewModelMappper.getOldCahcerEntitytoNewModel(
                          oldCacheEntity,
                          prayerData
                      )
                  Log.d(TAG, "getBlogsTest: $mixedDataList")
                  mixedDataList.map { prayerDao.insert(cacheMapperPrayer.mapToEntity(it)) }

              }
              val cacheEntity = prayerDao.get()
              emit(DataState.Success(cacheMapperPrayer.mapFromEntityList(cacheEntity)))

          } catch (e: Exception) {
              Log.e(TAG, "getBlogsTest: $e")
              emit(DataState.Success(cacheMapperPrayer.mapFromEntityList(getCachedValue())))
              emit(DataState.Error(e))
          }
      }*/

    suspend fun getCurrentDayPrayersData(currentDate: String): Flow<DataState<PrayerData>> = flow {
        emit(DataState.Loading)
        try {
            Log.d(TAG, "getCurrentDayPrayersData: $currentDate")
            val cacheEntity = prayerDao.getCurrentDayPrayersData(currentDate)
            emit(DataState.Success(cacheMapperPrayer.mapFromEntity(cacheEntity)))

        } catch (e: Exception) {
            Log.d(TAG, "getCurrentDayPrayersData: $e")
            emit(DataState.Error(e))
        }
    }

    suspend fun getPrayersData(): Flow<DataState<List<PrayerData>>> = flow {
        emit(DataState.Loading)
        try {
            val cacheEntitys = prayerDao.get()
            emit(DataState.Success(cacheMapperPrayer.mapFromEntityList(cacheEntitys)))

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun updateIsAlarm(
        isAlarmSetForFajar: Boolean,
        isAlarmSetForDhur: Boolean,
        isAlarmSetForAsr: Boolean,
        isAlarmSetForMaghrib: Boolean,
        isAlarmSetForIsha: Boolean,
        timestamp: String
    ): Flow<DataState<Int>> =
        flow {
            emit(DataState.Loading)
            var cacheEntitys = 0
            try {
                CoroutineScope(IO).launch {
                    cacheEntitys = prayerDao.updateIsAlarm(
                        isAlarmSetForFajar,
                        isAlarmSetForDhur,
                        isAlarmSetForAsr,
                        isAlarmSetForMaghrib,
                        isAlarmSetForIsha,
                        timestamp
                    )
                }

                emit(DataState.Success(cacheEntitys))
            } catch (e: Exception) {
                Log.d(TAG, "updateIsAlarm: $e")
                emit(DataState.Error(e))
            }
        }

    //    suspend fun getBlogsTest(lat: String, lng: String): Flow<DataState<List<PrayerData>>> = flow {
//        emit(DataState.Loading)
//        try {
//            val dataNetworkEntity =
//                prayerRetrofit.get1(lat, lng, "2", "2022")
//            val prayerData =
//                networkMapperRetrofit.mapFromEntityList(dataNetworkEntity.data)
//            val OldCacheEntity = prayerDao.get()
//            delay(3000)
//            prayerData.map { prayerDao.insert(cacheMapperPrayer.mapToEntity(it)) }
//
//            val prayerMixedData =
//                oldCacheMapperToPrayer.mapFromEntityList(OldCacheEntity, prayerData)
//            Log.d(TAG, "getBlogsTest: $prayerMixedData")
//
//
//            prayerMixedData.map { prayerDao.insert(cacheMapperPrayer.mapToEntity(it)) }
//
//            val cacheEntity = prayerDao.get()
//            emit(DataState.Success(cacheMapperPrayer.mapFromEntityList(cacheEntity)))
//
//        } catch (e: Exception) {
//            emit(DataState.Success(cacheMapperPrayer.mapFromEntityList(getCachedValue())))
//            emit(DataState.Error(e))
//        }
//    }
    private suspend fun getCachedValue(): List<PrayerCacheEntity> {
        return prayerDao.get()
    }
}