package com.github.robfletcher.compass

import spock.lang.Ignore

class CompileSpec extends CompassPluginSpec {

  @Ignore("compass fails with no source files")
  def "compile is up to date for an empty sourceset"() {
    given:
    directory("src/main/sass")

    when:
    runTasks COMPILE_TASK_NAME

    then:
    wasUpToDate ":$COMPILE_TASK_NAME"
  }

  def "compiles a basic .scss stylesheet"() {
    given:
    file("src/main/sass/basic.scss") << '''
      $font: Georgia, serif;
      body { font-family: $font; }
    '''

    when:
    runTasksSuccessfully COMPILE_TASK_NAME

    then:
    with(stylesheet("build/stylesheets/basic.css")) {
      item(0).cssText == "body { font-family: Georgia, serif }"
    }
  }

  def "a subsequent execution is up-to-date"() {
    given:
    file("src/main/sass/basic.scss") << 'body { font-family: Georgia, serif; }'

    and:
    runTasksSuccessfully COMPILE_TASK_NAME

    expect:
    fileExists "build/stylesheets/basic.css"

    when:
    runTasksSuccessfully COMPILE_TASK_NAME

    then:
    wasUpToDate ":$COMPILE_TASK_NAME"
  }

  def "can specify the sass dir"() {
    given:
    buildFile << """
      compass {
        sassDir = file("src/sass")
      }
    """

    and:
    file("src/sass/main.scss") << '''body { font-family: Georgia, serif; }'''

    when:
    runTasksSuccessfully COMPILE_TASK_NAME

    then:
    with(stylesheet("build/stylesheets/main.css")) {
      item(0).cssText == "body { font-family: Georgia, serif }"
    }
  }

  def "can specify the css dir"() {
    given:
    buildFile << """
      compass {
        cssDir = file("build/css")
      }
    """

    and:
    file("src/main/sass/main.scss") << '''body { font-family: Georgia, serif; }'''

    when:
    runTasksSuccessfully COMPILE_TASK_NAME

    then:
    with(stylesheet("build/css/main.css")) {
      item(0).cssText == "body { font-family: Georgia, serif }"
    }
  }
}
