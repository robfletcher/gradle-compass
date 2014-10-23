package co.freeside.gradle.compass

import com.github.jrubygradle.JRubyExec
import org.gradle.api.Plugin
import org.gradle.api.Project

class CompassPlugin implements Plugin<Project> {

  private static final String TASK_GROUP_NAME = "compass"
  private static final String CONFIGURATION_NAME = "compass"

  public static final String DEFAULT_SASS_DIR = "src/main/sass"
  public static final String DEFAULT_CSS_DIR = "build/stylesheets"

  @Override
  void apply(Project project) {
    project.apply plugin: "com.github.jruby-gradle.base"
    project.configurations.create CONFIGURATION_NAME

    project.task("compassCompile", type: JRubyExec) {
      group TASK_GROUP_NAME
      description "Compile Sass stylesheets to CSS"
      inputs.dir DEFAULT_SASS_DIR
      outputs.dir DEFAULT_CSS_DIR
      jrubyArgs "-S"
      script "compass"
      scriptArgs "compile", "--sass-dir", DEFAULT_SASS_DIR, "--css-dir", DEFAULT_CSS_DIR
      configuration CONFIGURATION_NAME
    }

    project.task("compassWatch", type: JRubyExec) {
      group TASK_GROUP_NAME
      description "Compile Sass stylesheets to CSS when they change"
    }

    project.task("compassClean", type: JRubyExec) {
      group TASK_GROUP_NAME
      description "Remove generated files and the sass cache"
      outputs.dir DEFAULT_CSS_DIR
      jrubyArgs "-S"
      script "compass"
      scriptArgs "clean", "--sass-dir", DEFAULT_SASS_DIR, "--css-dir", DEFAULT_CSS_DIR
      configuration CONFIGURATION_NAME
    }
  }
}
