name := "shardingSingleton"

version := "0.1"

scalaVersion := "2.13.8"

val AkkaVersion = "2.6.18"
val AkkaHttpVersion = "10.2.7"

libraryDependencies += "com.typesafe.akka" %% "akka-cluster-sharding-typed" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http-caching" % AkkaHttpVersion
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.9"
libraryDependencies += "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion