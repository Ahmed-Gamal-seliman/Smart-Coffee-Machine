package com.example.coffeemachine.data.model

import com.example.data.model.BaseDto
import com.google.gson.annotations.SerializedName

data class HeatingDto(
    @SerializedName("state")
    val state: String
): BaseDto()