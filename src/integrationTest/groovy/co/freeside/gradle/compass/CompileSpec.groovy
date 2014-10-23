package co.freeside.gradle.compass

import spock.lang.Ignore

class CompileSpec extends CompassPluginSpec {

  @Ignore("compass fails with no source files")
  def "compile is up to date for an empty sourceset"() {
    given:
    directory("src/main/sass")

    when:
    run COMPILE_TASK_NAME

    then:
    upToDate ":$COMPILE_TASK_NAME"
  }

  def "compiles a basic .scss stylesheet"() {
    given:
    file("src/main/sass/basic.scss") << '''
      $font: Georgia, serif;
      body { font-family: $font; }
    '''

    when:
    run COMPILE_TASK_NAME

    then:
    with(stylesheet("build/stylesheets/basic.css")) {
      item(0).cssText == "body { font-family: Georgia, serif }"
    }
  }

  def "a subsequent execution is up-to-date"() {
    given:
    file("src/main/sass/basic.scss") << 'body { font-family: Georgia, serif; }'

    and:
    run COMPILE_TASK_NAME

    expect:
    fileExists "build/stylesheets/basic.css"

    when:
    run COMPILE_TASK_NAME

    then:
    upToDate ":$COMPILE_TASK_NAME"
  }
}
