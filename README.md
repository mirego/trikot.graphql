# :warning: Warning: this project as been moved
See [the new trikot repository](https://github.com/mirego/trikot) for releases starting from 3.0.0.

# Trikot.graphql
**Incubating**

Multiplaform graphql query implementation.
- Use Kotlinx.serialization to deserialize objects
- No codegen for now


# Sending a graphql request

##### 1 - Create the models for your response
```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class DataResponse<T>(val data: T)

@Serializable
data class FooResponse(val foo: Foo)

@Serializable
data class Foo(val __typename: String, val id: String)
```

##### 2 - Create a graphql query
```kotlin
class FooQuery(fooId: String) :
    AbstractGraphqlQuery<DataResponse<FooResponse>>(DataResponse.serializer(FooResponse.serializer())) {

    override val variables: Map<String, Any>? =
        mutableMapOf<String, Any>("fooId" to fooId)

    override val query = """
    query(${'$'}fooId: String!) {
        foo(fooId: ${'$'}fooId) {
            id
            __typename
        }
    }
    """
}
```

##### 3 - Execute your query
```kotlin
val query = GraphqlQueryPublisher(FooQuery("3"))
query.execute()
query.subscribe(cancellableManager) {
    print(it.data.foo.id) // 3
}
```

# Using with Trikot.datasources
```kotlin
val myDataSource = GraphqlDataSource<DataResponse<FooResponse>>(GraphqlPublisherFactoryImpl())
val dataSourceState = myDataSource.read(GraphqlQueryDataSourceRequest(FooQuery("3"), "cachableId-3"))
```

## Installation
##### Import dependencies
```groovy
    api "com.mirego.trikot:graphql:$trikot_graphql_version"
    jvm "com.mirego.trikot:graphql-jvm:$trikot_graphql_version"
    js "com.mirego.trikot:graphql-js:$trikot_graphql_version"
    iosx64 "com.mirego.trikot:graphql-iosx64:$trikot_graphql_version"
    iosarm64 "com.mirego.trikot:graphql-iosarm64:$trikot_graphql_version"
```
