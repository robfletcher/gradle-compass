package co.freeside.gradle.compass

import com.google.common.collect.ImmutableList
import groovy.transform.CompileStatic
import org.gradle.api.file.FileCollection

@CompileStatic
class ScriptArgumentsBuilder {

  private final CompassTask task
  private final ImmutableList.Builder<String> arguments = ImmutableList.builder()

  ScriptArgumentsBuilder(CompassTask task) {
    this.task = task
  }

  ScriptArgumentsBuilder add(String flag, boolean value) {
    if (value) {
      arguments.add(flag)
    }
    return this
  }

  ScriptArgumentsBuilder add(String flag, String value) {
    if (value) {
      arguments.add(flag).add(value)
    }
    return this
  }

  ScriptArgumentsBuilder add(String flag, File value) {
    if (value) {
      arguments.add(flag).add(value.path)
    }
    return this
  }

  ScriptArgumentsBuilder add(String flag, FileCollection value) {
    value?.files?.each {
      arguments.add(flag).add(it.path)
    }
    return this
  }

  List<String> toArgumentList() {
    arguments.build()
  }
}
