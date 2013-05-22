package org.gradle.plugins.compass

class CompassExtension {

	File gemPath
	String encoding

	File cssDir
	File sassDir
	File imagesDir
	File javascriptsDir
	File fontsDir

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
