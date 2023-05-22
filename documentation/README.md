# SWEN30006 Assignment2: Report
**Group Members:**
| Name | ID | Email |
| ---- | -- | ----- |
| Tahmin Ahmed | 1146663 | tahmin@student.unimelb.edu.au |
| Duvindra Dissanayake | 1152586 | duvindrasanj@student.unimelb.edu.au |
| Sugon (Tony) Chrisjaroon | 1128883 | schristjaroo@student.unimelb.edu.au |

This report looks at a design analysis of a refactor and extension of the game
"PacMan in the MultiVerse" into a new game "PacMan in the TorusVerse" as the second assignment of the subject
SWEN30006, 2023. To reflect on the learning outcomes of the subject, both GoF
and GRASP design patterns have been put into consideration during the
refactoring process.

First, we identify the structure of the initial codebase, then we discuss the
new design decisions made for the refactor and the reasons they were made.
The following diagrams have been provided to better visualize the changes made:
- A domain class diagram for capturing the covering the domain concepts
relating to the autoplayer and game levels/maps for PacMan in the TorusVerse
- A static design model (i.e., a design class diagram) for documenting your
design relating to the autoplayer and game levels/maps for PacMan in the
TorusVerse.
- Additional static and/or dynamic design models to support P1 and P2 above as you judge useful
and appropriate

```
Page Break here
```

## Initial codebase structure
The initial codebase contains the source directories for two separate programs:
1. The source code of the simple version of "PacMan in the MultiVerse"
2. The source code of a GUI to be used as a map maker

While the task is to integrate the GUI into the PacMan game and implement the
new functionality of the TorusVerse version, simply porting the GUI into a
badly designed codebase of the original game could potentially make for a worse
design. Thus, to make the best results, first we will analyze the simple
version of the game and check what changes can be made.

### Simple version of the game
#### The problem
Initially, all the game logic, including:
are all in the `Game` class. This strongly goes against the following GRASP
principles:
1. Low Coupling - every individual object interaction happens in the `Game` class.
This means that any changes made to any other class would cause a massive
impact to the entire program, thus making it harder to maintain
2. High Cohesion - The `Game` class does more than one well-defined job,
thus leaving its purpose very unfocused around separate responsibilities. This
makes it difficult to change and maintian the code; since responsibilities are
closely knit together, extending the game into the TorusVerse version could
require an entire system revamp
3. Creator - object initialization for all individual monsters and PacMan
happens in the `Game` class. This makes it difficult to implement and maintain
new monsters and ensure any new logic is followed everywhere within the source code

#### What changes can be made?
A table of considerations are made for all classes. For each consideration, the resulting design pattern chosen is proposed:
| Consideration to make | Pattern |
| --------------------- | ----------------- |
| Does any Class in the codebase need refactoring and simplification? | `Facade` | 
| Does any Class get an "evolution"; a new version of itself with potentially advanced features? | `Decorator` |
| Does any Class have an increasingly high amount of parameters, notably during initialization? | `Builder` |
| Should any Class adapt compatibility with any interface? | `Adapter` |
| Does any Class contain only a single shared instance | `Singleton` |
| Deos the class contain subclasses where its instantiation logic is irrelevant to where it's being used? | `Factory` |

**Note:** While no class really *needs* any patterns to be functional, the word "need" refers to any decision which is more meaningful to the semantics of the program.

After going through each class, the following design decisions have been made:
| Class | Pattern | Reason |
| ----- | ------- | ------ |
| `Game` | Facade | Provides a higher-level interface for the interaction of monsters, simplifying the logic in the `Game` class |
| `Game` | Facade | Provides a higher-level interface for the interaction of Pills, simplifying the logic in the `Game` class |
| `Game` | Singleton | Used to enforce that only one game is ever created during runtime |
| `Monster` | Factory | Hide the implementation logic of each monster to let external code focus on the usage of the `Monster` rather than its initialization |
| `Monster` | Adapter | Allow polymorphic behavior between itself and `PacActor` |
| `PacActor` | Adapter | Allow polymorphic behavior between itself and `Monster` |
| `PacActor` | Singleton | Prevent any new instance to ever be created and enforce it during development time and runtime |




