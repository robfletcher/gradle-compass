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
    project.afterEvaluate {
      def configuration = project.configurations.getByName(CONFIGURATION_NAME)
      if (!configuration.dependencies.any { it.name == "compass" }) {
        project.dependencies.add(CONFIGURATION_NAME, "rubygems:compass:+")
      }
    }

    project.extensions.create("compass", CompassExtension, project)

    project.task("compassCompile", type: JRubyExec) {
      println "compiling from ${project.compass.getSassDir()}"
      group TASK_GROUP_NAME
      description "Compile Sass stylesheets to CSS"
      inputs.dir project.compass.getSassDir()
      outputs.dir project.compass.getCssDir()
      jrubyArgs "-S"
      script "compass"
      scriptArgs "compile", "--sass-dir", project.compass.getSassDir(), "--css-dir", project.compass.getCssDir()
      configuration CONFIGURATION_NAME
    }

    project.task("compassWatch", type: JRubyExec) {
      group TASK_GROUP_NAME
      description "Compile Sass stylesheets to CSS when they change"
    }

    project.task("compassClean", type: JRubyExec) {
      group TASK_GROUP_NAME
      description "Remove generated files and the sass cache"
      outputs.dir project.compass.getCssDir()
      jrubyArgs "-S"
      script "compass"
      scriptArgs "clean", "--sass-dir", project.compass.getSassDir(), "--css-dir", project.compass.getCssDir()
      configuration CONFIGURATION_NAME
    }

    project.task("compassVersion", type: JRubyExec) {
      group TASK_GROUP_NAME
      description "Print out version information"
      jrubyArgs "-S"
      script "compass"
      scriptArgs "version"
      configuration CONFIGURATION_NAME
    }
  }
}
