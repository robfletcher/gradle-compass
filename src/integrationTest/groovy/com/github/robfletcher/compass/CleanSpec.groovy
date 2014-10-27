package com.github.robfletcher.compass

class CleanSpec extends CompassPluginSpec {

  def setup() {
    file("src/main/sass/file1.scss") << 'body { font-family: Georgia, serif; }'
    file("src/main/sass/file2.scss") << 'body { font-size: 16px; }'
    run COMPILE_TASK_NAME
  }

  def "deletes compiled CSS files"() {
    expect:
    fileExists "build/stylesheets/file1.css"
    fileExists "build/stylesheets/file2.css"

    when:
    run CLEAN_TASK_NAME

    then:
    !fileExists("build/stylesheets/file1.css")
    !fileExists("build/stylesheets/file2.css")
  }

  def "a subsequent execution is up-to-date"() {
    given:
    run CLEAN_TASK_NAME

    when:
    run CLEAN_TASK_NAME

    then:
    upToDate ":$CLEAN_TASK_NAME"
  }
}
