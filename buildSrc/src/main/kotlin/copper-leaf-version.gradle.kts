
// Setup default project values
//----------------------------------------------------------------------------------------------------------------------
var projectVersion: ProjectVersion by project.extra
projectVersion = getProjectVersion(logChanges = project === rootProject)

version = projectVersion
