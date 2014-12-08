package com.github.robfletcher.compass

import com.github.jrubygradle.JRubyExec
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin

class CompassPlugin implements Plugin<Project> {

  public static final String TASK_GROUP_NAME = "compass"
  public static final String CONFIGURATION_NAME = "compass"

  @Override
  void apply(Project project) {
    project.apply plugin: BasePlugin
    project.apply plugin: "com.github.jruby-gradle.base"
    project.apply plugin: "com.github.johnrengelman.processes"

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

    project.task("compassWatchStart", type: CompassFork) {
      command "watch"
      outputs.upToDateWhen { false }
    }

    project.task("compassWatchStop").doLast {
      project.tasks.findByName("compassWatchStart").processHandle.abort()
    }

    project.task("compassWatch", dependsOn: "compassWatchStart").doLast {
      group TASK_GROUP_NAME
      description "Compile Sass stylesheets to CSS when they change"
      project.tasks.findByName("compassWatchStart").processHandle.waitForFinish()
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

    project.tasks.withType(CompassTaskOptions) { CompassTaskOptions task ->
      task.conventionMapping.with {
        sourcemap = { extension.sourcemap }
        time = { extension.time }
        debugInfo = { extension.debugInfo }
        load = { extension.load }
        loadAll = { extension.loadAll }
        importPath = { extension.importPath }
        quiet = { extension.quiet }
        trace = { extension.trace }
        force = { extension.force }
        boring = { extension.boring }
        config = { extension.config }
        sassDir = { extension.sassDir }
        cssDir = { extension.cssDir }
        imagesDir = { extension.imagesDir }
        javascriptsDir = { extension.javascriptsDir }
        fontsDir = { extension.fontsDir }
        env = { extension.environment }
        outputStyle = { extension.outputStyle }
        relativeAssets = { extension.relativeAssets }
        noLineComments = { extension.noLineComments }
        httpPath = { extension.httpPath }
        generatedImagesPath = { extension.generatedImagesPath }
        gemDir = { extension.gemDir }
      }
    }

    project.afterEvaluate {
      project.tasks.findByName("assemble").dependsOn("compassCompile")

      [extension.sassDir, extension.cssDir, extension.imagesDir, extension.javascriptsDir, extension.fontsDir].each {
        if (it) {
          it.mkdirs()
        }
      }
    }
  }
}
