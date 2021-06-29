
// Setup default project values
//----------------------------------------------------------------------------------------------------------------------
var projectVersion: ProjectVersion by project.extra
var shortVersion: String by project.extra
var releaseVersion: String by project.extra
var fullVersion: String by project.extra

projectVersion = getProjectVersion(logChanges = project === rootProject)
shortVersion = projectVersion.shortVersion
releaseVersion = projectVersion.releaseVersion
fullVersion = projectVersion.fullVersion

version = projectVersion
