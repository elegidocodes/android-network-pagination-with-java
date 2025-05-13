# Android Network Pagination Example (With Java)

This project demonstrates how to implement **network pagination** in an Android application using the `Paging` library, `Retrofit`, `RxJava3`, and `Hilt`. It showcases how to load and display paginated data from a remote API, handle loading and error states, and manage dependencies cleanly.

---

### 🚀 Features

* **Paginated Data**: Efficient loading of large datasets using Paging3 with RxJava3.
* **Error Handling**: Displays error messages and provides retry mechanisms.
* **Network Integration**: Fetches paginated movie data from a remote API using Retrofit.
* **Smooth UI**: Includes Swipe-to-Refresh support.
* **Image Loading**: Uses Glide for efficient image rendering.
* **Dependency Injection**: Powered by Hilt for scalable and testable architecture.

---

### 🧱 Project Structure

* **ViewModel**: Manages business logic and emits `PagingData<Movie>` streams.
* **Adapters**: `MovieAdapter` handles the paginated data display; `MovieLoadStateAdapter` manages loading/error UI.
* **API Layer**: Uses Retrofit + RxJava3 for asynchronous network calls.
* **MainActivity**: Collects UI state from the ViewModel and binds it to the RecyclerView.

---

### 🛠 Required Dependencies

#### Retrofit

```gradle
// Retrofit
implementation(libs.retrofit)
implementation(libs.converter.scalars)
implementation(libs.logging.interceptor)
implementation(libs.gson)
implementation(libs.converter.gson)
```

```toml
[versions]
retrofit = "2.11.0"
loggingInterceptor = "4.9.0"
gson = "2.11.0"

[libraries]
retrofit = { module = "com.squareup.retrofit2:retrofit", version.ref = "retrofit" }
converter-scalars = { module = "com.squareup.retrofit2:converter-scalars", version.ref = "retrofit" }
logging-interceptor = { module = "com.squareup.okhttp3:logging-interceptor", version.ref = "loggingInterceptor" }
gson = { module = "com.google.code.gson:gson", version.ref = "gson" }
converter-gson = { module = "com.squareup.retrofit2:converter-gson", version.ref = "retrofit" }
```

#### RxJava3

```gradle
// RxJava3
implementation(libs.paging.runtime)
implementation(libs.paging.rxjava3)
implementation(libs.adapter.rxjava3)
```

```toml
[versions]
pagingRuntime = "3.3.6"
adapterRxjava3 = "2.11.0"

[libraries]
paging-runtime = { module = "androidx.paging:paging-runtime", version.ref = "pagingRuntime" }
paging-rxjava3 = { module = "androidx.paging:paging-rxjava3", version.ref = "pagingRuntime" }
adapter-rxjava3 = { module = "com.squareup.retrofit2:adapter-rxjava3", version.ref = "adapterRxjava3" }
```

#### Hilt Dagger

```gradle
// Hilt Dagger
implementation(libs.hilt.android)
annotationProcessor(libs.hilt.compiler)
```

```toml
[versions]
hiltDragger = "2.56.2"
hiltAndroid = "2.56.2"

[libraries]
hilt-android = { module = "com.google.dagger:hilt-android", version.ref = "hiltAndroid" }
hilt-compiler = { module = "com.google.dagger:hilt-compiler", version.ref = "hiltAndroid" }

[plugins]
hilt-dragger = { id = "com.google.dagger.hilt.android", version.ref = "hiltDragger" }
```

#### ViewModel & LiveData

```gradle
// ViewModel and LiveData
implementation(libs.lifecycle.viewmodel.ktx)
implementation(libs.lifecycle.livedata.ktx)
```

```toml
[versions]
lifecycleViewmodelKtx = "2.9.0"

[libraries]
lifecycle-viewmodel-ktx = { module = "androidx.lifecycle:lifecycle-viewmodel-ktx", version.ref = "lifecycleViewmodelKtx" }
lifecycle-livedata-ktx = { module = "androidx.lifecycle:lifecycle-livedata-ktx", version.ref = "lifecycleViewmodelKtx" }
```

#### Swipe to Refresh

```gradle
implementation(libs.swiperefreshlayout)
```

```toml
[versions]
swiperefreshlayout = "1.1.0"

[libraries]
swiperefreshlayout = { module = "androidx.swiperefreshlayout:swiperefreshlayout", version.ref = "swiperefreshlayout" }
```

#### Glide

```gradle
implementation(libs.glide)
annotationProcessor(libs.compiler)
```

```toml
[versions]
glide = "4.14.2"

[libraries]
glide = { module = "com.github.bumptech.glide:glide", version.ref = "glide" }
compiler = { module = "com.github.bumptech.glide:compiler", version.ref = "glide" }
```

---

### ⚙️ Gradle Configuration

#### `build.gradle.kts` (App module)

```kotlin
plugins {
    alias(libs.plugins.android.application)
    id("com.google.dagger.hilt.android")
}

android {
    ...
    buildFeatures {
        dataBinding = true
    }
}
```

#### `settings.gradle.kts` or project-level `build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.hilt.dragger) apply false
}
```

---

### 📱 AndroidManifest.xml

Don’t forget to add the Internet permission for network access:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

### 🧪 How It Works

* The ViewModel initializes a `Pager` and exposes a `Flowable<PagingData<Movie>>`.
* `MovieAdapter` displays each movie item using Glide for the poster image.
* `MovieLoadStateAdapter` shows loading/error UI at the top or bottom of the list.
* `MainActivity` collects and displays data, managing refresh logic and error retry.

---

### 🛠️ Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/elegidocodes/android-network-pagination-with-java.git
   ```

2. Open the project in Android Studio.

3. Sync Gradle.

4. Run the app on an emulator or physical device.

---

### 🤝 Contributing

Feel free to open issues or pull requests for bug fixes, enhancements, or new features.

---

### 📄 License

Licensed under the MIT License. See [LICENSE](https://mit-license.org/) for details.

---

Let me know if you want this formatted as a Markdown file (`README.md`) or included in your repo structure.
