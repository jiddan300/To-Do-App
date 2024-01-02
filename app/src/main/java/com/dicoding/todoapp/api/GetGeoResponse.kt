package com.dicoding.todoapp.api

import com.google.gson.annotations.SerializedName

data class GetGeoResponse(

	@field:SerializedName("region")
	val region: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null,

	@field:SerializedName("ip")
	val ip: String? = null,

	@field:SerializedName("accuracy")
	val accuracy: Int? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("timezone")
	val timezone: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("country_code")
	val countryCode: String? = null,

	@field:SerializedName("country_code3")
	val countryCode3: String? = null,

	@field:SerializedName("area_code")
	val areaCode: String? = null,

	@field:SerializedName("continent_code")
	val continentCode: String? = null,

	@field:SerializedName("organization_name")
	val organizationName: String? = null,

	@field:SerializedName("organization")
	val organization: String? = null,

	@field:SerializedName("asn")
	val asn: Int? = null,
)
