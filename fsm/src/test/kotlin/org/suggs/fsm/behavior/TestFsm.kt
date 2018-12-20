package org.suggs.fsm.behavior.builders

import org.suggs.fsm.behavior.BehavioredClassifier
import org.suggs.fsm.behavior.builders.EventBuilder.Companion.anEventCalled
import org.suggs.fsm.behavior.builders.RegionBuilder.Companion.aRegionCalled
import org.suggs.fsm.behavior.builders.TransitionBuilder.Companion.anExternalTransitionCalled
import org.suggs.fsm.behavior.builders.TriggerBuilder.Companion.aTriggerCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aCompositeStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aFinalStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.aSimpleStateCalled
import org.suggs.fsm.behavior.builders.VertexBuilder.Companion.anInitialPseudoStateCalled

class TestFsm {

    companion object {


        fun buildTestFsm(): BehavioredClassifier {

            return StateMachineBuilder.aStateMachineCalled("State Machine").withRegion(
                    aRegionCalled("R0")
                            .withVertices(
                                    anInitialPseudoStateCalled("R0_IS"),
                                    aCompositeStateCalled("R0_S0"),
                                    aCompositeStateCalled("R0_S1")
                                            .withRegion(aRegionCalled("R1")),
                                    aSimpleStateCalled("R0_S2"),
                                    aSimpleStateCalled("R0_S3"),
                                    aFinalStateCalled("R0_FS"))
                            .withTransitions(
                                    anExternalTransitionCalled("R0_T1").startingAt("R0_IS").endingAt("R0_S0"),
                                    anExternalTransitionCalled("R0_T2").startingAt("R0_S0").endingAt("R0_S1")
                                            .withTriggers(aTriggerCalled("R0_T2_TG1").firedWith(anEventCalled("event1"))),
                                    anExternalTransitionCalled("R0_T3").startingAt("R0_S0").endingAt("R0_S2")
                                            .withTriggers(aTriggerCalled("R0_T3_TG1").firedWith(anEventCalled("event2"))),
                                    anExternalTransitionCalled("R0_T4").startingAt("R0_S1").endingAt("R0_FS")
                                            .withTriggers(aTriggerCalled("R0_T4_TG1").firedWith(anEventCalled("event3"))),
                                    anExternalTransitionCalled("R0_T5").startingAt("R0_S2").endingAt("R0_FS")
                                            .withTriggers(aTriggerCalled("R0_T5_TG1").firedWith(anEventCalled("event4")))
                            )).build()
        }
    }
}