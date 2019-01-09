package org.suggs.fsm.behavior.builders

import org.slf4j.LoggerFactory
import org.suggs.fsm.behavior.BehavioredClassifier
import org.suggs.fsm.behavior.builders.BehaviorBuilder.Companion.aBehaviorCalled
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.StateMachineBuilder.Companion.aStateMachineCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.anExternalTransitionCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aCompositeStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled

class TestFsm {

    companion object {

        private val log = LoggerFactory.getLogger(this::class.java)

        fun buildSimpleStateMachine(): BehavioredClassifier {

            return aStateMachineCalled("Simple State Machine").withRegion(
                    aRegionCalled("R0")
                            .withVertices(
                                    anInitialPseudoStateCalled("IS"),
                                    aSimpleStateCalled("S0"),
                                    aFinalStateCalled("FS"))
                            .withTransitions(
                                    anExternalTransitionCalled("T1").startingAt("IS").endingAt("S0"),
                                    anExternalTransitionCalled("T2").startingAt("S0").endingAt("FS").withTriggers(aTriggerCalled("T2_TG1").firedWith(anEventCalled("event1")))
                            )
            ).build()
        }

        fun buildFsmWithAutomatedTransitions(): BehavioredClassifier {
            return aStateMachineCalled("State Machine with automated transitions").withRegion(
                    aRegionCalled("region0")
                            .withVertices(
                                    anInitialPseudoStateCalled("R0_IS"),
                                    aSimpleStateCalled("R0_S1")
                                            .withDeferrableTriggers(
                                                    aTriggerCalled("R0_S1_DE1").firedWith(anEventCalled("e1")),
                                                    aTriggerCalled("R0_S1_DE2").firedWith(anEventCalled("e2")),
                                                    aTriggerCalled("R0_S1_DE3").firedWith(anEventCalled("e3"))),
                                    aSimpleStateCalled("R0_S2")
                                            .withDeferrableTriggers(
                                                    aTriggerCalled("R0_S2_DE1").firedWith(anEventCalled("e1")),
                                                    aTriggerCalled("R0_S2_DE2").firedWith(anEventCalled("e2")),
                                                    aTriggerCalled("R0_S2_DE3").firedWith(anEventCalled("e3"))),
                                    aSimpleStateCalled("R0_S3")
                                            .withDeferrableTriggers(
                                                    aTriggerCalled("R0_S3_DE1").firedWith(anEventCalled("e2")),
                                                    aTriggerCalled("R0_S3_DE2").firedWith(anEventCalled("e3"))),
                                    aSimpleStateCalled("R0_S4")
                                            .withDeferrableTriggers(
                                                    aTriggerCalled("R0_S4_DE1").firedWith(anEventCalled("e3"))
                                            ),
                                    aSimpleStateCalled("R0_S5"),
                                    aFinalStateCalled("R0_FS")
                            )
                            .withTransitions(
                                    anExternalTransitionCalled("R0_T1")
                                            .startingAt("R0_IS")
                                            .endingAt("R0_S1"),
                                    anExternalTransitionCalled("R0_T2")
                                            .startingAt("R0_S1")
                                            .endingAt("R0_S2")
                                            .withTriggers(aTriggerCalled("R0_T2_TG1")
                                                    .firedWith(anEventCalled("e10")))
                                            .withEffects(aBehaviorCalled("R0_T2_B1")
                                                    .withAction { it -> log.debug("""actioning $it""") }),
                                    anExternalTransitionCalled("R0_T3")
                                            .startingAt("R0_S2")
                                            .endingAt("R0_S3")
                                            .withEffects(aBehaviorCalled("R0_T3_B1")
                                                    .withAction { it -> log.debug("""actioning $it""") }),
                                    anExternalTransitionCalled("R0_T4")
                                            .startingAt("R0_S3")
                                            .endingAt("R0_S4")
                                            .withTriggers(aTriggerCalled("R0_T4_TG1")
                                                    .firedWith(anEventCalled("e1")))
                                            .withEffects(aBehaviorCalled("R0_T4_B1")
                                                    .withAction { it -> log.debug("""actioning $it""") }),
                                    anExternalTransitionCalled("R0_T5")
                                            .startingAt("R0_S4")
                                            .endingAt("R0_S5")
                                            .withTriggers(aTriggerCalled("R0_T5_TG1")
                                                    .firedWith(anEventCalled("e2")))
                                            .withEffects(aBehaviorCalled("R0_T5_B1")
                                                    .withAction { it -> log.debug("""actioning $it""") }),
                                    anExternalTransitionCalled("R0_T6")
                                            .startingAt("R0_S5")
                                            .endingAt("R0_FS")
                                            .withTriggers(aTriggerCalled("R0_T6_TG1")
                                                    .firedWith(anEventCalled("e3")))
                                            .withEffects(aBehaviorCalled("R0_T6_B1")
                                                    .withAction { it -> log.debug("""actioning $it""") })
                            )
            ).build()
        }

        fun buildFsmWithEntryAndExitBehaviors(): BehavioredClassifier {
            return aStateMachineCalled("Simple State Machine with entry and exit behaviors").withRegion(
                    aRegionCalled("R0")
                            .withVertices(
                                    anInitialPseudoStateCalled("R0_IS"),
                                    aSimpleStateCalled("R0_S1")
                                            .withEntryBehavior(aBehaviorCalled("R0_S1_ENTRYACTION").withAction { it -> log.debug("entry action for $it") })
                                            .withExitBehavior(aBehaviorCalled("R0_S1_EXITACTION").withAction { it -> log.debug("exit action for $it") }),
                                    aSimpleStateCalled("R0_S2"),
                                    aSimpleStateCalled("R0_S3")
                            )
                            .withTransitions(
                                    anExternalTransitionCalled("R0_T1").startingAt("R0_IS").endingAt("R0_S1")
                                            .withTriggers(aTriggerCalled("R0_T1_TG1").firedWith(anEventCalled("e1"))),
                                    anExternalTransitionCalled("R0_T2").startingAt("R0_S1").endingAt("R0_S2")
                                            .withTriggers(aTriggerCalled("R0_T2_TG1").firedWith(anEventCalled("internalEvent1"))),
                                    anExternalTransitionCalled("R0_T3").startingAt("R0_S1").endingAt("R0_S3")
                                            .withTriggers(aTriggerCalled("R0_T3_TG1").firedWith(anEventCalled("internalEvent2")))
                            )
            ).build()
        }

        fun buildNestedStateStateMachine(): BehavioredClassifier {
            return aStateMachineCalled("Composite State machine").withRegion(
                    aRegionCalled("R0")
                            .withVertices(
                                    anInitialPseudoStateCalled("R0_IS"),
                                    aSimpleStateCalled("R0_S0"),
                                    aCompositeStateCalled("R0_S1")
                                            .withRegion(aRegionCalled("R1")
                                                    .withVertices(
                                                            anInitialPseudoStateCalled("R1_IS"),
                                                            aSimpleStateCalled("R1_S1"),
                                                            aFinalStateCalled("R1_FS"))
                                                    .withTransitions(

                                                    )
                                            ),
                                    aFinalStateCalled("R0_FS"))
                            .withTransitions(
                                    anExternalTransitionCalled("R0_T0").startingAt("R0_IS").endingAt("R0_S0"),
                                    anExternalTransitionCalled("R0_T1").startingAt("R0_S0").endingAt("R0_S1"),
                                    anExternalTransitionCalled("R0_T2").startingAt("R0_S1").endingAt("R0_FS")
                            )
            ).build()
        }
    }
}