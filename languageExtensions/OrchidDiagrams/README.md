---
---

## Orchid Diagrams
---

Create flowcharts and sequence diagrams using the PlantUML markup language.

### Using PlantUML with Orchid

Simply include this plugin and you're all set. Orchid will now recognize files with file extensions of `uml` and compile 
them as PlantUML, no further configuration necessary. The "standard" PlantUML diagram must start with `@startuml` and 
end with `@enduml`, but if this is not present it will be added for you. 

Note that some diagram types require GraphViz to be installed on your local machine to work properly.