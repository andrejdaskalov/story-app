package com.adaskalov.storyapp.modules

import com.adaskalov.storyapp.data.api.GenerativeApi
import com.adaskalov.storyapp.data.api.GenerativeApiProd
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class GeminiModule {

    @Binds
    abstract fun bindGenerativeApi(
        generativeApiProd: GenerativeApiProd
    ): GenerativeApi
}