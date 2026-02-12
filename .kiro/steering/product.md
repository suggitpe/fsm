# Product Overview

This is a Finite State Machine (FSM) library implemented in Kotlin. The library provides a framework for defining and executing state machines based on UML 2.5 specifications.

## Core Capabilities

- Define state machines with states, transitions, triggers, and events
- Support for composite states with nested regions
- Entry and exit behaviors for states
- Guard conditions and constraints on transitions
- Deferred event handling
- UML diagram generation for state machines
- Execution environment for processing business events through state machines

## Domain Model

The library models FSM concepts including:
- StateMachine: Top-level container with regions
- States: Simple states, composite states, pseudo states, and final states
- Transitions: Connections between states with triggers and guards
- Events: Business events that drive state changes
- Behaviors: Actions executed on entry/exit or during transitions
- Execution context: Runtime environment for state machine execution
