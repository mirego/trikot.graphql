package com.mirego.trikot.graphql

import com.mirego.trikot.foundation.CommonJSExport
import com.mirego.trikot.http.HttpHeaderProvider
import com.mirego.trikot.http.HttpConfiguration
import com.mirego.trikot.streams.reactive.executable.ExecutablePublisher

@CommonJSExport
interface GraphqlPublisherFactory {
    fun <T> create(
        request: GraphqlQuery<T>,
        httpHeaderProvider: HttpHeaderProvider = HttpConfiguration.defaultHttpHeaderProvider
    ): ExecutablePublisher<T>
}
