uniform.project12("eff-parallel-3-0-4", "au.com.cba.zbi.effparallel", "zbi")

uniformDependencySettings

strictDependencySettings

uniformAssemblySettings

uniform.ghsettings

updateOptions := updateOptions.value.withCachedResolution(true)

libraryDependencies += "org.atnos" %% "eff" % "3.0.4"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")