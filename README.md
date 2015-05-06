# Gradle Compass plugin

A [SASS][sass] / [Compass][compass] plugin for [Gradle][gradle]. The plugin uses JRuby to install and run Compass.

## Tasks

The plugin adds the following tasks:

### compileSass

Compiles all SASS files. Equivalent to the `compass compile` command. The task supports incremental build.

### watchSass

Compiles and watches all SASS files. Equivalent to the `compass watch` command. The task runs in the background so it can be added to the dependency chain for the task that starts your application.

### installCompass

Installs the Compass Ruby gem and any additional gems you specify. This is executed automatically by the `compileSass` and `watchSass` tasks.

## Installation

Add the [JAR](https://github.com/holisticon/gradle-compass/releases/latest) to your nexus and apply the plugin:


```groovy
apply plugin: 'compass'

buildscript {
	...
	dependencies {
        	classpath 'org.gradle.plugins:gradle-compass:1.0.15'
	}
}

repositories {
	mavenCentral() // or any other repository containing JRuby
}
```

## Configuration

General configuration for the plugin goes inside a `compass` block in your build file and will apply to all tasks. You can also specify configuration properties on the individual tasks (for example you may want to set `environment = "production"` on the *compileSass* and `debugInfo = true` on *watchSass*). As a minimum you must specify the target directory where compiled CSS files should go and the source directories containing SASS/SCSS files. For example:

```groovy
compass {
    	sourcesMirror = 'http://nexus.example.com:8080/content/repositories/rubygems/'
	cssDir = file('public/styles')
	sassDir = file('src/main/sass')
}
```

### Configuration parameters

The full set of parameters supported by the plugin is…

#### JRuby options

* `jrubyVersion`: the version of JRuby to install. The current Default is to `1.7.8`.
* `gemPath`: the directory where the plugin will install Ruby gems. Defaults to `<project dir>/.jruby/gems`.
* `gems`: the names of the gems to install. The default is `["compass"]`. Optionally, you can specify the version of the gem using the format `gem-name:version`.
* `encoding`: the file encoding used by JRuby. The default is your platform default encoding.
* `jvmArgs`: additional arguments to pass to the JVM when running JRuby. The default is blank.

#### Paths
* `sourcesMirror`: the ruby gem mirror to use 
* `cssDir` **required**: the target directory where compiled CSS is output. Equivalent to `--css-dir`.
* `sassDir` **required**: the source directory where you keep *.scss* and/or *.sass* files. Equivalent to `--sass-dir`.
* `imagesDir`: the source directory where you keep image files. Equivalent to `--images-dir`.
* `javascriptsDir`: the source directory where you keep JavaScript files. You don't need to specify this unless you have Compass extensions in your scripts. Equivalent to `--javascripts-dir`.
* `fontsDir`: the source directory where you keep fonts. Equivalent to `--fonts-dir`.
* `importPath`: a set of directories containing other Sass stylesheets. Specifying this allows you to reference those stylesheets in `@import` directives. Equivalent to `--additional_import_paths`.

#### Compilation options

* `debugInfo`: if *true* (the default) Compass adds debug information to the compiled CSS. Equivalent to `--debug-info` if set to *true* or `--no-debug-info` if set to *false*.
* `dryRun`: if *true* Compass will just output what it will do without actually doing it. Equivalent to `--dry-run`.
* `environment`: sets default options when set to *'development'* (the default) or *'production'*. Equivalent to `--environment`.
* `force`: if *true* Compass will overwrite existing files. Equivalent to `--force`.
* `noLineComments`: if *true* Compass will not output line comments to the compiled CSS files. Equivalent to `--no-line-comments`.
* `outputStyle`: selects the style for compiled CSS. One of *nested*, *expanded*, *compact* (the default) or *compressed*. Eqivalent to `--output-style`.
* `projectType`: tells Compass what type of app you have. Valid options are *stand_alone* (the default) or *rails*. Equivalent to `--app`.
* `relativeAssets`: if *true* Compass will generate relative urls to assets. Equivalent to `--relative-assets`.

#### Command line output

* `boring`: if *true* colorized output is disabled. Equivalent to `--boring`.
* `quiet`: if *true* Compass output is suppressed. Equivalent to `--quiet`.
* `trace`: if *true* Compass displays full stack traces on error. Equivalent to `--trace`.

## Using with other tasks

You will typically want to execute the Compass tasks as part of a larger build. For example this configuration will run `compileSass` as part of `processResources`, `watchSass` as part of the [Application plugin][app-plugin]'s `run` task and clean the output of `compileSass` as part of `clean`:

```groovy
processResources.inputs.files compileSass
run.dependsOn watchSass
clean.dependsOn cleanCompileSass
```

# Version history

### 1.0.10

* added ability to specify gem versions.

### 1.0.9

* use additional gems without needing a *config.rb* file.

### 1.0.8

* added ability to specify additional gems.

### 1.0.7

* made `javascriptsDir`, `imagesDir` and `importPath` optional.

### 1.0.6

* added ability to specify `importPath`.

### 1.0.5

* added various command line options. Thanks [Ben Groves](http://github.com/bgroves).

### 1.0.4

* added ability to specify file encoding used by JRuby.

[app-plugin]:http://www.gradle.org/docs/current/userguide/application_plugin.html
[compass]:http://compass-style.org/
[gradle]:http://gradle.org/
[sass]:http://sass-lang.com/
