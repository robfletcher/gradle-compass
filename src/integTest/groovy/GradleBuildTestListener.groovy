

import org.gradle.BuildAdapter
import org.gradle.api.invocation.Gradle
import org.gradle.initialization.ClassLoaderRegistry

class GradleBuildTestListener extends BuildAdapter {
    @Override
    void projectsLoaded(final Gradle gradle) {
        gradle.rootProject.services.get(ClassLoaderRegistry.class).rootClassLoader.allowPackage("org.gradle.plugins.compass")
    }
}