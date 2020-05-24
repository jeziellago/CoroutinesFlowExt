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

// or create publisher from source Flow
val myFlowEmitter = flow { emit(1) } // "source" Flow (emitter)

val publisher = FlowPublisher(source = myFlowEmitter)

// or use Flow extension
val publisher = myFlowEmitter.asPublisher()
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

### Easy collect for StateFlow
Use `observeState`:
```kotlin
coroutineScope.observeState(stateFlow) {
  // collect value
}
``` 
Instead of:
```kotlin
coroutineScope.launch {
    stateFlow.collect {
        // collect value
    }
}
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
    implementation 'com.github.jeziellago:CoroutinesFlowExt:0.1.1'
}
```
