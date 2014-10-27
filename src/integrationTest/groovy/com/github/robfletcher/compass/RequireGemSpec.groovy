package com.github.robfletcher.compass

class RequireGemSpec extends CompassPluginSpec {

  def setup() {
    buildFile << """
      dependencies {
        compass "rubygems:modernizr-mixin:+"
      }
    """
  }

  def "uses compass extensions from gems"() {
    given:
    file("src/main/sass/extended.scss") << '''
      @import "modernizr";
      body {
        @include yep(rgba) {
          background-color: rgba(0, 0, 0, 0.2);
        }
      }
    '''

    when:
    run COMPILE_TASK_NAME

    then:
    with(stylesheet("build/stylesheets/extended.css")) {
      item(0).cssText == "*.rgba body { background-color: rgba(0, 0, 0, 0.2) }"
    }
  }

}
