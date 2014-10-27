package co.freeside.gradle.compass

class TaskIntegrationSpec extends CompassPluginSpec {

  def setup() {
    buildFile << """
      apply plugin: "groovy"
    """
  }

  def "compassCompile is run as part of the assemble task"() {
    given:
    file("src/main/sass/main.scss") << '''
      body { font-family: Georgia, serif; }
    '''

    when:
    run "assemble"

    then:
    standardOutput.any {
      it == ":compassCompile"
    }
  }

  def "compassClean is run as part of the clean task"() {
    when:
    run "clean"

    then:
    standardOutput.any {
      it == ":compassClean"
    }
  }

}
