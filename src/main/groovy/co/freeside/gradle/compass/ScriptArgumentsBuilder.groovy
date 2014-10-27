package co.freeside.gradle.compass

import groovy.transform.TypeChecked
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.file.FileCollection

@TypeChecked
class ScriptArgumentsBuilder {

  private final CompassTask task
  private final List<String> arguments = []

  ScriptArgumentsBuilder(CompassTask task) {
    this.task = task
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

  ScriptArgumentsBuilder addGem(String flag, Dependency value) {
    if (value && value.name != "compass") {
      arguments << flag << value.name
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
      it.name != "compass"
    } each {
      addGem(flag, it)
    }
    return this
  }

  List<String> toArgumentList() {
    arguments.asImmutable()
  }
}
