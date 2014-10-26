package co.freeside.gradle.compass

import com.google.common.collect.ImmutableList
import org.gradle.api.file.FileCollection

import static com.google.common.base.CaseFormat.LOWER_HYPHEN
import static com.google.common.base.CaseFormat.UPPER_CAMEL

class ScriptArgumentsBuilder {

  private final caseConverter = LOWER_HYPHEN.converterTo(UPPER_CAMEL)
  private final CompassTask task
  private final arguments = ImmutableList.builder()

  ScriptArgumentsBuilder(CompassTask task) {
    this.task = task
  }

  ScriptArgumentsBuilder bool(String flag) {
    if (task."is${caseConverter.convert(flag)}"()) {
      arguments.add(flag)
    }
    return this
  }

  ScriptArgumentsBuilder str(String flag) {
    String value = task."get${caseConverter.convert(flag)}"()
    if (value) {
      arguments.add(flag).add(value)
    }
    return this
  }

  ScriptArgumentsBuilder file(String flag) {
    File value = task."get${caseConverter.convert(flag)}"()
    if (value) {
      arguments.add(flag).add(value.path)
    }
    return this
  }

  ScriptArgumentsBuilder files(String flag) {
    FileCollection value = task."get${caseConverter.convert(flag)}"()
    value?.files?.each {
      arguments.add(flag).add(it.path)
    }
    return this
  }

  List<String> toArgumentList() {
    arguments.build()
  }
}
