package org.suggs.fsm.behavior

import org.slf4j.LoggerFactory
import org.suggs.fsm.behavior.builders.BehaviorBuilder.Companion.aBehaviorCalled
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.StateMachineBuilder.Companion.aStateMachineCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.aTransitionCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aCompositeStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalState
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoState

object FsmPrototypes {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun fsmWithEntryAndExitBehaviorsPrototype() =
        aStateMachineCalled("context").withRegion(
            aRegionCalled("region0")
                .withVertices(
                    anInitialPseudoState(),
                    aSimpleStateCalled("state1")
                        .withEntryBehavior(aBehaviorCalled("doLoggingEntryAction()").withAction { log.debug("entry action for $it") })
                        .withExitBehavior(aBehaviorCalled("doLoggingExitAction()").withAction { log.debug("exit action for $it") }),
                    aSimpleStateCalled("state2"),
                    aSimpleStateCalled("state3")
                )
                .withTransitions(
                    aTransitionCalled("region0_trans1").startingAtInitialState().endingAt("state1").triggeredBy(anEventCalled("e1")),
                    aTransitionCalled("region0_trans2").startingAt("state1").endingAt("state2").triggeredBy(anEventCalled("internalEven_trigger1")),
                    aTransitionCalled("region0_trans3").startingAt("state1").endingAt("state3").triggeredBy(anEventCalled("internalEven_trigger2"))
                )
        )

    fun nestedStateStateMachinePrototype() =
        aStateMachineCalled("context").withRegion(
            aRegionCalled("region0")
                .withVertices(
                    anInitialPseudoState(),
                    aSimpleStateCalled("state1"),
                    aSimpleStateCalled("state2"),
                    aCompositeStateCalled("state3").withRegion(
                        aRegionCalled("region1").withVertices(
                            anInitialPseudoState(),
                            aSimpleStateCalled("state1"),
                            aSimpleStateCalled("state2")
                        )
                            .withTransitions(
                                aTransitionCalled("trans1").startingAtInitialState().endingAt("state1"),
                                aTransitionCalled("trans2").startingAt("state1").endingAt("state1").triggeredBy(anEventCalled("New Data")),
                                aTransitionCalled("trans3").startingAt("state1").endingAt("state2").triggeredBy(anEventCalled("enough"))
                            )
                    ),
                    aFinalState()
                )
                .withTransitions(
                    aTransitionCalled("trans1").startingAtInitialState().endingAt("state1"),
                    aTransitionCalled("trans2").startingAt("state1").endingAt("state2").triggeredBy(anEventCalled("succeeded")),
                    aTransitionCalled("trans3").startingAt("state1").endingAtFinalState().triggeredBy(anEventCalled("aborted")),
                    aTransitionCalled("trans4").startingAt("state2").endingAtFinalState().triggeredBy(anEventCalled("aborted")),
                    aTransitionCalled("trans5").startingAt("state2").endingAt("state3").triggeredBy(anEventCalled("succeeded")),
                    aTransitionCalled("trans6").startingAt("state3").endingAtFinalState().triggeredBy(anEventCalled("succeeded")),
                    aTransitionCalled("trans7").startingAt("state3").endingAtFinalState().triggeredBy(anEventCalled("aborted")),
                    aTransitionCalled("trans8").startingAt("state3").endingAt("state3").triggeredBy(anEventCalled("failed"))
                )

        )


}