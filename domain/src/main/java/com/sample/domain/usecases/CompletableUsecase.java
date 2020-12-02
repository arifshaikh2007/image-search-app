package com.sample.domain.usecases;

import io.reactivex.Completable;

interface CompletableUsecase<T> {
    Completable execute(T param);
}
