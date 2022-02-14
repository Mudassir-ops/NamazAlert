package com.example.dummymvvmproject.data.remote.retrofit


import com.example.dummymvvmproject.model.PrayerData
import com.example.dummymvvmproject.util.EntityMapper
import javax.inject.Inject

import com.example.dummymvvmproject.data.remote.retrofit.PrayerDataNetworkEntity

class NetworkMapperRetrofit
@Inject
constructor() :
    EntityMapper<com.example.dummymvvmproject.data.remote.retrofit.PrayerDataNetworkEntity, PrayerData> {

    override fun mapFromEntity(entity: com.example.dummymvvmproject.data.remote.retrofit.PrayerDataNetworkEntity): PrayerData {
        return PrayerData(
            Fajr = entity.timingsNetworkEntity?.fajr,
            Sunrise = entity.timingsNetworkEntity?.sunrise,
            Dhuhr = entity.timingsNetworkEntity?.dhuhr,
            Asr = entity.timingsNetworkEntity?.asr,
            Sunset = entity.timingsNetworkEntity?.sunset,
            Maghrib = entity.timingsNetworkEntity?.maghrib,
            Isha = entity.timingsNetworkEntity?.isha,
            Imsak = entity.timingsNetworkEntity?.imsak,
            Midnight = entity.timingsNetworkEntity?.midnight,
            grogreanWeekday = entity.dateNetworkEntity?.gregorianNetworkEntity?.weekday?.en,
            grogreanDate = entity.dateNetworkEntity?.gregorianNetworkEntity?.date,
            grogreanMonth = entity.dateNetworkEntity?.gregorianNetworkEntity?.monthNetworkEntity?.en,
            hijriWeekday = entity.dateNetworkEntity?.hijriNetworkEntity?.weekday?.ar,
            hijriDate = entity.dateNetworkEntity?.hijriNetworkEntity?.date,
            hijriMonth = entity.dateNetworkEntity?.hijriNetworkEntity?.monthNetworkEntity?.ar,
            timezone = entity.metaNetworkEntity?.timezone,
            timestamp = entity.dateNetworkEntity?.timestamp,
            day = entity.dateNetworkEntity?.gregorianNetworkEntity?.day,
            latitude = entity.metaNetworkEntity?.latitude,
            longitude = entity.metaNetworkEntity?.longitude,
        )
    }

    fun mapFromEntityList(entityPrayers: List<com.example.dummymvvmproject.data.remote.retrofit.PrayerDataNetworkEntity>): List<PrayerData> {
        return entityPrayers.map { mapFromEntity(it) }
    }

    override fun mapToEntity(domainModel: PrayerData): com.example.dummymvvmproject.data.remote.retrofit.PrayerDataNetworkEntity {
        return com.example.dummymvvmproject.data.remote.retrofit.PrayerDataNetworkEntity(

        )
    }


}





















