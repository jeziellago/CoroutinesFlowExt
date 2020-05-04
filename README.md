# CoroutinesFlowExt
Reactive components with Kotlin Flow

### FlowPublisher and FlowSubscriber
```kotlin
// Publisher
val publisher = flowPublisher {
    emit(1)
    emit(2)
    emit(3)
}
```

```kotlin
// Create Subscribers

// use `flowSubscriberOf`
val subscriberA = flowSubscriberOf<Int>(publisher)

// or
val subscriberB = FlowSubscriber<Int>()
publisher.subscribe(subscriberB)
```

```kotlin
// Receive values as Flow
val flowFromA = subscriberA.asFlow()
val flowFromB = subscriberB.asFlow()
```

```kotlin
// Start emitter from publisher 
publisher.open()
```
```kotlin
// Unsubscribe any receiver
publisher.unsubscribe(subscriberA)
```

## Add dependencies
- Project `build.gradle` 
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
- Module `build.gradle` 
```
dependencies {
    implementation 'com.github.jeziellago:CoroutinesFlowExt:0.1.0'
}
```
