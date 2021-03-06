package com.uhi5d.statibud.domain.model.currentuser


import com.google.gson.annotations.SerializedName

data class ExplicitContent(
    @SerializedName("filter_enabled")
    val filterEnabled: Boolean?,
    @SerializedName("filter_locked")
    val filterLocked: Boolean?
)