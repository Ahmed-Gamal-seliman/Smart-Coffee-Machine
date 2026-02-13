package com.example.coffeemachine.data.mapper

import com.example.coffeemachine.data.model.BrewDto
import com.example.coffeemachine.data.model.HeatingDto
import com.example.coffeemachine.domain.model.Brew
import com.example.coffeemachine.domain.model.Heating

fun HeatingDto.toHeating(): Heating{
    return Heating(
        state = state
    ).apply {
        code=codeDto
        message=messageDto
    }
}

fun BrewDto.toBrew(): Brew {
    return Brew(
        state = state
    ).apply {
        code=codeDto
        message=messageDto
    }
}