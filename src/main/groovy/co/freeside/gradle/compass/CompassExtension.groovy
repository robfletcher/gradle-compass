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
  File config
  FileCollection importPath
  boolean sourcemap
  boolean time
  boolean debugInfo
  boolean quiet
  boolean trace
  boolean force
  boolean boring
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
