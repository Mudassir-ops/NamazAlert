package com.example.dummymvvmproject.util

interface EntityMapperOldToNewReplace<OldcacheEntity, NewPrayerModel, DomainModel> {

    fun mapFromEntity(entity: OldcacheEntity, newPrayerModel: NewPrayerModel): DomainModel

}