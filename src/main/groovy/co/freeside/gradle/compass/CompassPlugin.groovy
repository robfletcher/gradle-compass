package co.freeside.gradle.compass

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project

@CompileStatic
class CompassPlugin implements Plugin<Project> {
  @Override
  void apply(Project project) {
    project.apply(plugin: "com.lookout.jruby")
  }
}
