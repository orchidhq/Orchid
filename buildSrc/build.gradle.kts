plugins {
    java
    `kotlin-dsl`
}

allprojects {
    repositories {
        jcenter()
    }
}

group = "com.eden"

tasks.create("publishPlugins") {
    doLast {}
}

dependencies {
    runtime(project(":orchidPlugin"))
}
