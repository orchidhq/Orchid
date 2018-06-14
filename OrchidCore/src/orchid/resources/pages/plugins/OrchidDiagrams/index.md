---
official: true
noDocs: true
description: Create flowcharts and sequence diagrams using the PlantUML markup language.
images:
  - src: http://res.cloudinary.com/orchid/image/upload/c_scale,w_300,e_blur:150/v1524974867/plugins/diagrams.jpg
    alt: Diagrams
    caption: Photo by William Iven on Unsplash
---

### Using PlantUML with Orchid

Simply include this plugin and you're all set. Orchid will now recognize files with file extensions of `uml` and `puml`
and compile them as PlantUML, no further configuration necessary. The "standard" PlantUML diagram must start with 
`@startuml` and end with `@enduml`, but if this is not present it will be added for you. Plant UML diagrams are compiled
into SVG format and embedded directly into your site's HTML.

Note that some diagram types require GraphViz to be installed on your local machine to work properly.

[Examples](#examples)

[TOC levels=4]

### Examples

The following examples are taken directly from the official [PlantUML docs](http://plantuml.com/)

#### Sequence Diagram

[source](http://plantuml.com/sequence-diagram)

{% highlight 'text' %}
Alice -> Bob: Authentication Request
Bob --> Alice: Authentication Response

Alice -> Bob: Another authentication Request
Alice <-- Bob: another authentication Response
{% endhighlight %}

{% filter compileAs('uml') %}
Alice -> Bob: Authentication Request
Bob --> Alice: Authentication Response

Alice -> Bob: Another authentication Request
Alice <-- Bob: another authentication Response
{% endfilter %}

#### Class Description

[source](http://plantuml.com/class-diagram)

{% highlight 'text' %}
class Dummy {
 -field1
 #field2
 ~method1()
 +method2()
}
{% endhighlight %}

{% filter compileAs('uml') %}
class Dummy {
 -field1
 #field2
 ~method1()
 +method2()
}
{% endfilter %}

#### Activity Diagram

[source](http://plantuml.com/activity-diagram-beta)

{% highlight 'text' %}
start
if (condition A) then (yes)
  :Text 1;
elseif (condition B) then (yes)
  :Text 2;
  stop
elseif (condition C) then (yes)
  :Text 3;
elseif (condition D) then (yes)
  :Text 4;
else (nothing)
  :Text else;
endif
stop
{% endhighlight %}

{% filter compileAs('uml') %}
start
if (condition A) then (yes)
  :Text 1;
elseif (condition B) then (yes)
  :Text 2;
  stop
elseif (condition C) then (yes)
  :Text 3;
elseif (condition D) then (yes)
  :Text 4;
else (nothing)
  :Text else;
endif
stop
{% endfilter %}

#### State Diagram

[source](http://plantuml.com/state-diagram)

{% highlight 'text' %}
scale 350 width
[*] --> NotShooting

state NotShooting {
  [*] --> Idle
  Idle --> Configuring : EvConfig
  Configuring --> Idle : EvConfig
}

state Configuring {
  [*] --> NewValueSelection
  NewValueSelection --> NewValuePreview : EvNewValue
  NewValuePreview --> NewValueSelection : EvNewValueRejected
  NewValuePreview --> NewValueSelection : EvNewValueSaved
  
  state NewValuePreview {
    State1 -> State2
  }  
}
{% endhighlight %}

{% filter compileAs('uml') %}
scale 350 width
[*] --> NotShooting

state NotShooting {
  [*] --> Idle
  Idle --> Configuring : EvConfig
  Configuring --> Idle : EvConfig
}

state Configuring {
  [*] --> NewValueSelection
  NewValueSelection --> NewValuePreview : EvNewValue
  NewValuePreview --> NewValueSelection : EvNewValueRejected
  NewValuePreview --> NewValueSelection : EvNewValueSaved

  state NewValuePreview {
    State1 -> State2
  } 
}
{% endfilter %}

#### Timing Diagram

[source](http://plantuml.com/timing-diagram)

{% highlight 'text' %}
robust "Web Browser" as WB
concise "Web User" as WU

WB is Initializing
WU is Absent

@WB
0 is idle
+200 is Processing
+100 is Waiting
WB@0 <-> @50 : {50 ms lag}

@WU
0 is Waiting
+500 is ok
@200 <-> @+150 : {150 ms}
{% endhighlight %}

{% filter compileAs('uml') %}
robust "Web Browser" as WB
concise "Web User" as WU

WB is Initializing
WU is Absent

@WB
0 is idle
+200 is Processing
+100 is Waiting
WB@0 <-> @50 : {50 ms lag}

@WU
0 is Waiting
+500 is ok
@200 <-> @+150 : {150 ms}
{% endfilter %}
