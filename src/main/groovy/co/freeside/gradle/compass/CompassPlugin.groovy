package co.freeside.gradle.compass

import com.github.jrubygradle.JRubyExec
import org.gradle.api.Plugin
import org.gradle.api.Project

class CompassPlugin implements Plugin<Project> {

  public static final String TASK_GROUP_NAME = "compass"
  public static final String CONFIGURATION_NAME = "compass"

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

    project.task("compassCompile", type: CompassTask) {
      group TASK_GROUP_NAME
      description "Compile Sass stylesheets to CSS"
      command "compile"
    }

    project.task("compassWatch", type: CompassTask) {
      group TASK_GROUP_NAME
      description "Compile Sass stylesheets to CSS when they change"
      command "watch"
    }

    project.task("compassClean", type: CompassTask) {
      group TASK_GROUP_NAME
      description "Remove generated files and the sass cache"
      command "clean"
    }

    project.task("compassVersion", type: JRubyExec) {
      group TASK_GROUP_NAME
      description "Print out version information"
      jrubyArgs "-S"
      script "compass"
      scriptArgs "version"
      configuration CONFIGURATION_NAME
    }

    project.task("compassConfig", type: CompassTask) {
      group TASK_GROUP_NAME
      description "Generate a configuration file"
      command "config"
    }

    def extension = project.extensions.create("compass", CompassExtension, project)

    project.tasks.withType(CompassTask) { CompassTask task ->
      task.conventionMapping.with {
        cssDir = { extension.cssDir }
        sassDir = { extension.sassDir }
        importPath = { extension.importPath }
        imagesDir = { extension.imagesDir }
        relativeAssets = { extension.relativeAssets }
      }
    }

    project.afterEvaluate {
      [extension.sassDir, extension.cssDir, extension.imagesDir].each {
        it.mkdirs()
      }
    }
  }
}
