// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    id("org.sonarqube") version "5.0.0.4638"
}

// Optional: Configure SonarQube globally (project info only)
sonarqube {
    properties {
        property("sonar.projectKey", "MapLibre-Android-Auto-Sample")
        property("sonar.projectName", "MapLibre-Android-Auto-Sample")
        property("sonar.host.url", "http://192.168.1.68:9000")
        // NOTE: Do NOT put the token here. It should be passed via CLI or Jenkins securely.
    }
}
