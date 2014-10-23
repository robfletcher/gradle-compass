package co.freeside.gradle.compass

import com.github.jrubygradle.JRubyExec
import org.gradle.api.Plugin
import org.gradle.api.Project

class CompassPlugin implements Plugin<Project> {

  private static final String TASK_GROUP_NAME = "compass"
  private static final String CONFIGURATION_NAME = "compass"

  @Override
  void apply(Project project) {
    project.apply plugin: "com.github.jruby-gradle.base"
    project.configurations.create CONFIGURATION_NAME
    project.extensions.create("compass", CompassExtension).with {
      sassDir = project.file("src/main/sass")
      cssDir = project.file("build/stylesheets")
    }

    project.task("compassCompile", type: JRubyExec) {
      println "compiling from $project.compass.sassDir"
      group TASK_GROUP_NAME
      description "Compile Sass stylesheets to CSS"
      inputs.dir project.compass.sassDir
      outputs.dir project.compass.cssDir
      jrubyArgs "-S"
      script "compass"
      scriptArgs "compile", "--sass-dir", project.compass.sassDir, "--css-dir", project.compass.cssDir
      configuration CONFIGURATION_NAME
    }

    project.task("compassWatch", type: JRubyExec) {
      group TASK_GROUP_NAME
      description "Compile Sass stylesheets to CSS when they change"
    }

    project.task("compassClean", type: JRubyExec) {
      group TASK_GROUP_NAME
      description "Remove generated files and the sass cache"
      outputs.dir project.compass.cssDir
      jrubyArgs "-S"
      script "compass"
      scriptArgs "clean", "--sass-dir", project.compass.sassDir, "--css-dir", project.compass.cssDir
      configuration CONFIGURATION_NAME
    }
  }
}
