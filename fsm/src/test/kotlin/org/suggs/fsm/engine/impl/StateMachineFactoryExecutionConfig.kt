package org.suggs.fsm.engine.impl

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.suggs.fsm.engine.uml2.constraints.ConstraintVisitor
import org.suggs.fsm.engine.uml2.constraints.basicbehaviors.BehaviourChecker
import org.suggs.fsm.engine.uml2.constraints.basicbehaviors.BehaviouredClassifierChecker
import org.suggs.fsm.engine.uml2.constraints.behaviorstatemachines.*
import org.suggs.fsm.engine.uml2.constraints.communications.EventChecker
import org.suggs.fsm.engine.uml2.constraints.communications.TriggerChecker
import org.suggs.fsm.engine.uml2.constraints.kernel.NamedElementChecker
import org.suggs.fsm.engine.uml2.optimisation.DefaultTransitionFactory
import org.suggs.fsm.engine.uml2.optimisation.ModelOptimiser
import org.suggs.fsm.event.BaseEventFactory
import org.suggs.fsm.framework.api.IStateMachineFactory
import org.suggs.fsm.framework.spi.impl.SpringBehavioredClassifierBuilder
import org.suggs.fsm.uml2.scribe.constraints.IConstraintVisitor
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser

@Configuration
open class StateMachineFactoryExecutionConfig {

    @Bean open fun stateMachineFactory(): IStateMachineFactory {
        val stateMachineFactory = StateMachineFactory()
        stateMachineFactory.behavioredClassifierBuilder = SpringBehavioredClassifierBuilder()
        stateMachineFactory.eventContextFactory = EventContextFactory()
        stateMachineFactory.eventFactory = BaseEventFactory()
        stateMachineFactory.constraintChecker = constraintVisitor()
        stateMachineFactory.modelOptimiser = modelOptimiser()
        return stateMachineFactory
    }

    @Bean open fun constraintVisitor(): IConstraintVisitor {
        val constraintVisitor = ConstraintVisitor()
        constraintVisitor.behaviorChecker = behaviorChecker()
        constraintVisitor.behavioredClassifierChecker = behavioredClassifierChecker()
        constraintVisitor.eventChecker = eventChecker()
        constraintVisitor.setFinalStateChecker(FinalStateChecker())
        constraintVisitor.namedElementChecker = NamedElementChecker()
        constraintVisitor.pseudoStateChecker = PseudoStateChecker()
        constraintVisitor.regionChecker = RegionChecker()
        constraintVisitor.stateChecker = StateChecker()
        constraintVisitor.stateMachineChecker = StateMachineChecker()
        constraintVisitor.transitionChecker = TransitionChecker()
        constraintVisitor.triggerChecker = TriggerChecker()
        constraintVisitor.vertexChecker = VertexChecker()
        return constraintVisitor
    }

    @Bean open fun modelOptimiser(): IModelOptimiser {
        val optimiser = ModelOptimiser()
        optimiser.transitionFactory = DefaultTransitionFactory()
        return optimiser
    }

    @Bean open fun eventChecker(): EventChecker = EventChecker()

    @Bean open fun behavioredClassifierChecker(): BehaviouredClassifierChecker = BehaviouredClassifierChecker()

    @Bean open fun behaviorChecker(): BehaviourChecker = BehaviourChecker()
}