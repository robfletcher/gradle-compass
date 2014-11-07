package com.github.robfletcher.compass

import groovy.transform.TypeChecked
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.file.FileCollection

@TypeChecked
class ScriptArgumentsBuilder {

  private final List<String> arguments = []

  ScriptArgumentsBuilder(CompassTask task) {
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
      it.name != "compass"
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
