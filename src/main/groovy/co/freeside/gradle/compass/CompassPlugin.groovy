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

    project.task("compassCompile", type: JRubyExec) {
      group TASK_GROUP_NAME
      description "Compile compass stylesheets"
      inputs.dir "src/main/sass"
      outputs.dir "build/stylesheets"
      jrubyArgs "-S"
      script "compass"
      scriptArgs "compile", "--sass-dir", "src/main/sass", "--css-dir", "build/stylesheets"
      configuration CONFIGURATION_NAME
    }
  }
}
