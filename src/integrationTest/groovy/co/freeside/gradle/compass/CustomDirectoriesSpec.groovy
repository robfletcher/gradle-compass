package co.freeside.gradle.compass

class CustomDirectoriesSpec extends CompassPluginSpec {

  def "can customize the sass dir"() {
    given:
    buildFile << """
      compass {
        sassDir = file("src/sass")
      }
    """

    and:
    file("src/sass/main.scss") << '''body { font-family: Georgia, serif; }'''

    when:
    run COMPILE_TASK_NAME

    then:
    with(stylesheet("build/stylesheets/main.css")) {
      item(0).cssText == "body { font-family: Georgia, serif }"
    }
  }

}
