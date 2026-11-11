plugins {
    id("com.android.application")
}

android {
    namespace = "app.nurwallpaper"
    compileSdk = 36

    defaultConfig {
        applicationId = "app.nurwallpaper"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
