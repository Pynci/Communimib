plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "it.unimib.communimib"
    compileSdk = 34

    defaultConfig {
        applicationId = "it.unimib.communimib"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.database)
    implementation(libs.volley)
    testImplementation(libs.junit)
    testImplementation("org.mockito:mockito-core:3.+")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Dipendenze per espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")

    implementation("commons-validator:commons-validator:1.7")
    implementation("commons-io:commons-io:2.15.0")

    //Dipendenze per firebase
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-messaging")


    //Dipendenze per room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    //Dipendenze per splash screen
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")

    //Dipendenze per material design bottom bar
    implementation("com.google.android.material:material:1.11.0")

    //Dipendenze per immagini
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //Dipendenza per GSON
    implementation("com.google.code.gson:gson:2.8.8")

    //Dipendenza per l'image slider
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")
}