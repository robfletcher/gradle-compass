package groovy.co.freeside.gradle.compass

import org.gololang.gradle.test.integration.framework.IntegrationSpec

class CompileSpec extends IntegrationSpec {

  final COMPILE_TASK_NAME = "compileCompass"

  def setup() {
    buildFile << """
      apply plugin: "compass"
    """
  }

  def "compile is up to date for an empty sourceset"() {
    when:
    run COMPILE_TASK_NAME

    then:
    upToDate ":$COMPILE_TASK_NAME"
  }

  def "compiles a basic .scss stylesheet"() {
    given:
    file("src/main/sass/basic.scss") << """
      body { font-size: 16 * 1px; }
    """

    when:
    run COMPILE_TASK_NAME

    then:
    file("build/css/basic.css").text == """
      body {
        font-size: 16px;
      }
    """
  }

}
