package com.sample.domain.usecases

import io.reactivex.Single

interface SingleUseCase<T> {
    fun execute(param: String): Single<T>
}