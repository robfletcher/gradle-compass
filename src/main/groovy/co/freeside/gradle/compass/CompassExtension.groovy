package co.freeside.gradle.compass

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.file.FileCollection

@CompileStatic
class CompassExtension {

  boolean sourcemap
  boolean time
  boolean debugInfo
  File load
  File loadAll
  FileCollection importPath
  boolean quiet
  boolean trace
  boolean force
  boolean boring
  File config
  File sassDir
  File cssDir
  File imagesDir
  File javascriptsDir
  File fontsDir
  String environment
  String outputStyle
  boolean relativeAssets
  boolean noLineComments
  String httpPath
  String generatedImagesPath

  CompassExtension(Project project) {
    cssDir = project.file("build/stylesheets")
    sassDir = project.file("src/main/sass")
  }

}
