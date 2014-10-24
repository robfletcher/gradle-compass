package co.freeside.gradle.compass

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.file.FileCollection

@CompileStatic
class CompassExtension {

  File cssDir
  File sassDir
  File imagesDir
  File javascriptsDir
  File fontsDir
  FileCollection importPath

  boolean relativeAssets
  boolean boring
  boolean debugInfo
  boolean dryRun
  boolean force
  boolean noLineComments
  boolean quiet
  boolean trace

  String environment
  String outputStyle
  String projectType

  CompassExtension(Project project) {
    cssDir = project.file("build/stylesheets")
    sassDir = project.file("src/main/sass")
  }

}
