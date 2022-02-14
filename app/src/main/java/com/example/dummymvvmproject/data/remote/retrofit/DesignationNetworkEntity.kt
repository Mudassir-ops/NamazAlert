package com.example.dummymvvmproject.data.remote.retrofit
import com.google.gson.annotations.SerializedName

data class DesignationNetworkEntity (

	@SerializedName("abbreviated") val abbreviated : String,
	@SerializedName("expanded") val expanded : String
)