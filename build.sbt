name := "spark-custom-transformer"

version := "1.0"

scalaVersion := "2.11.0"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

scalaSource in Compile := baseDirectory.value / "src"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.0.0",
  "org.apache.spark" %% "spark-sql" % "2.0.0",
  "org.apache.spark" %% "spark-hive" % "2.0.0",
  "org.apache.spark" %% "spark-streaming" % "2.0.0",
  "org.apache.spark" %% "spark-streaming-flume" % "2.0.0",
  "org.apache.spark" %% "spark-mllib" % "2.0.0",
  "org.apache.commons" % "commons-lang3" % "3.0",
  "com.github.scopt" %% "scopt" % "3.2.0"
)
libraryDependencies  ++= Seq(

	"com.github.wookietreiber" %% "scala-chart" % "latest.integration",
	"com.itextpdf" % "itextpdf" % "5.5.6",
	"org.jfree" % "jfreesvg" % "3.0"

  // other dependencies here
  //"org.scalanlp" %% "breeze" % "0.12",
  // native libraries are not included by default. add this if you want them (as of 0.7)
  // native libraries greatly improve performance, but increase jar sizes. 
  // It also packages various blas implementations, which have licenses that may or may not
  // be compatible with the Apache License. No GPL code, as best I know.
  //"org.scalanlp" %% "breeze-natives" % "0.12",
  // the visualization library is distributed separately as well. 
  // It depends on LGPL code.
  //  "org.scalanlp" %% "breeze-viz" % "0.12"
)

libraryDependencies ++= Seq(
  // "edu.berkeley.nlp" % "berkeleyparser" % "r32",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.4",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.4" classifier "models",
  "edu.stanford.nlp" % "stanford-parser" % "3.4"
)

resolvers ++= Seq(
  // other resolvers here
  // if you want to use snapshot builds (currently 0.12-SNAPSHOT), use this.
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)
