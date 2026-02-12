# Project Structure

## Module Organization

Single module project with one subproject:
- `fsmlib/` - Core finite state machine library

## Source Layout

Standard Kotlin/Gradle structure:

```
fsmlib/
├── src/
│   ├── main/kotlin/org/suggs/fsm/
│   │   ├── behavior/          # FSM behavior model (states, transitions, events)
│   │   │   ├── builders/      # Builder pattern implementations
│   │   │   └── traits/        # Kotlin interfaces for shared behaviors
│   │   ├── execution/         # Runtime execution environment
│   │   └── uml/              # UML diagram generation
│   └── test/kotlin/org/suggs/fsm/
│       ├── behavior/          # Behavior model tests
│       ├── execution/         # Execution environment tests
│       ├── stubs/            # Test stubs and fixtures
│       └── uml/              # UML generator tests
```

## Package Organization

### `org.suggs.fsm.behavior`
Core FSM model classes representing UML state machine concepts:
- State machine elements: StateMachine, Region, State (Simple/Composite/Final), PseudoState
- Transitions and triggers: Transition, Trigger, Event, Constraint
- Behaviors: Behavior, BehavioredClassifier
- Named elements: NamedElement, NamedElementContainer, Vertex

### `org.suggs.fsm.behavior.traits`
Kotlin interfaces defining shared capabilities:
- Enterable, Exitable - Entry/exit actions
- Fireable - Transition firing
- Named, Namespace, NamespaceRegisterable - Naming and registration
- Processable - Event processing
- RegionContainer - Region containment

### `org.suggs.fsm.behavior.builders`
Builder pattern implementations for constructing FSM elements

### `org.suggs.fsm.execution`
Runtime execution framework:
- FsmExecutionEnvironment - Main execution coordinator
- FsmExecutionContext - Execution state and context
- FsmStateManager - State persistence interface
- BusinessEvent, BusinessObjectReference - Business domain integration
- NamespaceObjectMapper - Name-to-object resolution

### `org.suggs.fsm.uml`
UML diagram generation utilities

## Test Organization

- Tests mirror main source structure
- `stubs/` package contains test doubles and fixtures
- Test prototypes in FsmPrototypes.kt for reusable test state machines
- Integration tests demonstrate complete FSM scenarios

## Naming Conventions

- Classes: PascalCase
- Files: Match class name with .kt extension
- Packages: lowercase, dot-separated
- Test files: ClassNameTest.kt pattern
