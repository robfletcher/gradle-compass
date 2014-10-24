package co.freeside.gradle.compass

class GemDependenciesSpec extends CompassPluginSpec {

  def "by default uses latest compass version"() {
    when:
    run "compassVersion"

    then:
    standartOutput.any {
      it == "Compass 1.0.1 (Polaris)"
    }
  }

  def "can specify compass version"() {
    given:
    buildFile << """
      dependencies {
        compass "rubygems:compass:$version"
      }
    """

    when:
    run "compassVersion"

    then:
    standartOutput.any {
      it == "Compass 0.12.6 (Alnilam)"
    }

    where:
    version = "0.12.6"
  }

}
