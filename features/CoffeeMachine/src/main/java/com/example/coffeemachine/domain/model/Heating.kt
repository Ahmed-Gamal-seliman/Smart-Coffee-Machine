package com.example.coffeemachine.domain.model

import com.example.domain.model.BaseDomain

data class Heating(
    val state: String
): BaseDomain()