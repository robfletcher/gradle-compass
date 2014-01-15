package org.gradle.plugins.compass

import org.gradle.api.file.FileCollection

class CompassExtension {

  File gemPath
  String encoding
  String jvmArgs
  String jrubyVersion
  Collection<String> gems

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

}
