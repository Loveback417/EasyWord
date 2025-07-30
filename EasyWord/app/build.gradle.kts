plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.easyword"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.easyword"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")//新增依赖Cradview
    implementation ("androidx.recyclerview:recyclerview:1.3.1")//新增依赖Recycleview
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")//表格
    implementation ("com.google.android.material:material:1.9.0") // 使用最新版本

    implementation("androidx.room:room-runtime:2.5.0")//Room
    annotationProcessor("androidx.room:room-compiler:2.5.0")

    // 图片处理
    implementation ("com.github.bumptech.glide:glide:4.13.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.2")
    // 权限处理
    implementation ("pub.devrel:easypermissions:3.0.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}