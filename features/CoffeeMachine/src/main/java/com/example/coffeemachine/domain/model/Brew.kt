package com.example.coffeemachine.domain.model

import com.example.domain.model.BaseDomain

data class Brew(
    val state: String
): BaseDomain()