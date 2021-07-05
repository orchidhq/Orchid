package com.eden.orchid.languages.diagrams

import com.eden.orchid.impl.generators.HomepageGenerator
import com.eden.orchid.strikt.pageWasRendered
import com.eden.orchid.testhelpers.OrchidIntegrationTest
import com.eden.orchid.testhelpers.withGenerator
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat

@DisplayName("Tests behavior of using Mermaid JS component")
class MermaidJsComponentTest : OrchidIntegrationTest(withGenerator<HomepageGenerator>(), DiagramsModule()) {

    @Test
    @DisplayName("Test that MermaidJS is added to the page")
    fun test01() {
        configObject(
            "theme",
            """
            |{
            |    "metaComponents": [
            |        { 
            |            "type": "mermaid",
            |            "selector": "pre code[class='language-mermaid-js']"
            |        }
            |    ]
            |}
            """.trimMargin()
        )

        resource(
            "homepage.md",
            """
            |```mermaid-js
            |graph TD;
            |    A-->B;
            |    A-->C;
            |    B-->D;
            |    C-->D;
            |```
            |
            |
            |```mermaid-js
            |pie title NETFLIX
            |         "Time spent looking for movie" : 90
            |         "Time spent watching it" : 10
            |```
            |
            |```mermaid-js
            |pie title What Voldemort doesn't have?
            |         "FRIENDS" : 2
            |         "FAMILY" : 3
            |         "NOSE" : 45
            |```
            |
            |```mermaid-js
            |sequenceDiagram
            |    Alice ->> Bob: Hello Bob, how are you?
            |    Bob-->>John: How about you John?
            |    Bob--x Alice: I am good thanks!
            |    Bob-x John: I am good thanks!
            |    Note right of John: Bob thinks a long<br/>long time, so long<br/>that the text does<br/>not fit on a row.
            |
            |    Bob-->Alice: Checking with John...
            |    Alice->John: Yes... John, how are you?
            |```
            |
            |```mermaid-js
            |graph LR
            |    A[Square Rect] -- Link text --> B((Circle))
            |    A --> C(Round Rect)
            |    B --> D{Rhombus}
            |    C --> D
            |```
            |
            |```mermaid-js
            |graph TB
            |    sq[Square shape] --> ci((Circle shape))
            |
            |    subgraph A
            |        od>Odd shape]-- Two line<br/>edge comment --> ro
            |        di{Diamond with <br/> line break} -.-> ro(Rounded<br>square<br>shape)
            |        di==>ro2(Rounded square shape)
            |    end
            |
            |    %% Notice that no text in shape are added here instead that is appended further down
            |    e --> od3>Really long text with linebreak<br>in an Odd shape]
            |
            |    %% Comments after double percent signs
            |    e((Inner / circle<br>and some odd <br>special characters)) --> f(,.?!+-*ز)
            |
            |    cyr[Cyrillic]-->cyr2((Circle shape Начало));
            |
            |     classDef green fill:#9f6,stroke:#333,stroke-width:2px;
            |     classDef orange fill:#f96,stroke:#333,stroke-width:4px;
            |     class sq,e green
            |     class di orange
            |```
            |
            |```mermaid-js
            |sequenceDiagram
            |    loop Daily query
            |        Alice->>Bob: Hello Bob, how are you?
            |        alt is sick
            |            Bob->>Alice: Not so good :(
            |        else is well
            |            Bob->>Alice: Feeling fresh like a daisy
            |        end
            |
            |        opt Extra response
            |            Bob->>Alice: Thanks for asking
            |        end
            |    end
            |```
            |
            |```mermaid-js
            |sequenceDiagram
            |    participant Alice
            |    participant Bob
            |    Alice->>John: Hello John, how are you?
            |    loop Healthcheck
            |        John->>John: Fight against hypochondria
            |    end
            |    Note right of John: Rational thoughts<br/>prevail...
            |    John-->>Alice: Great!
            |    John->>Bob: How about you?
            |    Bob-->>John: Jolly good!
            |```
            |
            |```mermaid-js
            |gantt
            |    dateFormat  YYYY-MM-DD
            |    title Adding GANTT diagram to mermaid
            |    excludes weekdays 2014-01-10
            |    
            |    section A section
            |    Completed task            :done,    des1, 2014-01-06,2014-01-08
            |    Active task               :active,  des2, 2014-01-09, 3d
            |    Future task               :         des3, after des2, 5d
            |    Future task2               :         des4, after des3, 5d
            |```
            |
            |```mermaid-js
            |classDiagram
            |    Class01 <|-- AveryLongClass : Cool
            |    Class03 *-- Class04
            |    Class05 o-- Class06
            |    Class07 .. Class08
            |    Class09 --> C2 : Where am i?
            |    Class09 --* C3
            |    Class09 --|> Class07
            |    Class07 : equals()
            |    Class07 : Object[] elementData
            |    Class01 : size()
            |    Class01 : int chimp
            |    Class01 : int gorilla
            |    Class08 <--> C2: Cool label
            |```
            |
            |```mermaid-js
            |gitGraph:
            |options
            |{
            |    "nodeSpacing": 150,
            |    "nodeRadius": 10
            |}
            |end
            |commit
            |branch newbranch
            |checkout newbranch
            |commit
            |commit
            |checkout master
            |commit
            |commit
            |merge newbranch
            |```
            """.trimMargin()
        )

        expectThat(execute())
            .pageWasRendered("/index.html")
    }
}
