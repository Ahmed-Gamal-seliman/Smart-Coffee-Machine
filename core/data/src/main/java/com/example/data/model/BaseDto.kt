package com.example.data.model

import com.google.gson.annotations.SerializedName

 open class BaseDto (
    @SerializedName("message") var messageDto: String="",
    @SerializedName("code") var codeDto: String=""
)