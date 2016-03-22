package com.github.robfletcher.compass

import groovy.transform.TypeChecked
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.file.FileCollection

import static com.github.robfletcher.compass.CompassPlugin.CONFIGURATION_NAME

@TypeChecked
class ScriptArgumentsBuilder {

  static List<String> compassArgs(CompassTaskOptions task) {
    new ScriptArgumentsBuilder(task)
      .addFlag("--sourcemap", task.getSourcemap())
      .addFlag("--time", task.getTime())
      .addFlag("--debug-info", task.getDebugInfo())
      .addGems("--require", task.project.configurations.getByName(CONFIGURATION_NAME).dependencies)
      .addFile("--load", task.getLoad())
      .addFile("--load-all", task.getLoadAll())
      .addDirs("--import-path", task.getImportPath())
      .addFlag("--quiet", task.getQuiet())
      .addFlag("--trace", task.getTrace())
      .addFlag("--force", task.getForce())
      .addFlag("--boring", task.getBoring())
      .addFile("--config", task.getConfig())
      .addFile("--sass-dir", task.getSassDir())
      .addFile("--css-dir", task.getCssDir())
      .addFile("--images-dir", task.getImagesDir())
      .addFile("--javascripts-dir", task.getJavascriptsDir())
      .addFile("--fonts-dir", task.getFontsDir())
      .addString("--environment", task.getEnv())
      .addString("--output-style", task.getOutputStyle())
      .addFlag("--relative-assets", task.getRelativeAssets())
      .addFlag("--no-line-comments", task.getNoLineComments())
      .addString("--http-path", task.getHttpPath())
      .addString("--generated-images-path", task.getGeneratedImagesPath())
      .toArgumentList()
  }

  private final List<String> arguments = []

  ScriptArgumentsBuilder(CompassTaskOptions task) {
    arguments << task.command
  }

  ScriptArgumentsBuilder addFlag(String flag, boolean value) {
    if (value) {
      arguments << flag
    }
    return this
  }

  ScriptArgumentsBuilder addString(String flag, String value) {
    if (value) {
      arguments << flag << value
    }
    return this
  }

  ScriptArgumentsBuilder addFile(String flag, File value) {
    if (value) {
      arguments << flag << value.path
    }
    return this
  }

  ScriptArgumentsBuilder addDirs(String flag, FileCollection value) {
    value?.files?.each {
      addFile(flag, it)
    }
    return this
  }

  ScriptArgumentsBuilder addGems(String flag, DependencySet dependencies) {
    dependencies.findAll { Dependency it ->
      it.name != "compass" && it.name != 'jruby-complete'
    } each {
      arguments << flag << it.name
    }
    return this
  }

  ScriptArgumentsBuilder addAll(List<String> args) {
    arguments.addAll(args)
    return this
  }

  List<String> toArgumentList() {
    arguments.asImmutable()
  }
}
