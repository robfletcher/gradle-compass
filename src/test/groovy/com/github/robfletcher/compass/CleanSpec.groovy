package com.github.robfletcher.compass

import spock.lang.*

class CleanSpec extends CompassPluginSpec {

  def setup() {
    file("src/main/sass/file1.scss") << 'body { font-family: Georgia, serif; }'
    file("src/main/sass/file2.scss") << 'body { font-size: 16px; }'
    runTasks COMPILE_TASK_NAME
  }

  @Unroll
  def "#taskName deletes compiled CSS files"() {
    expect:
    fileExists "build/stylesheets/file1.css"
    fileExists "build/stylesheets/file2.css"

    when:
    runTasks taskName

    then:
    !fileExists("build/stylesheets/file1.css")
    !fileExists("build/stylesheets/file2.css")

    where:
    taskName << ["cleanCompassCompile", "clean"]
  }
}
