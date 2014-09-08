package org.gradle.plugins.compass

class RubyGem {
  String name
  String version
  Collection<String> opts = []

  RubyGem() {} // XXX enables the Map constructor

  public RubyGem(String gemString) {
    if (gemString?.contains(":")) {
      (name, version) = gemString.split(":", 2)
    } else {
      name = gemString
    }
  }
}
