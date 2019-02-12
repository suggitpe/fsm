package org.suggs.fsm.behavior.builders

import org.slf4j.LoggerFactory
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

object FsmPrototypes {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun simpleFsmPrototype() =
            aStateMachineCalled("context").withRegion(
                    RegionBuilder.aRegionCalled("region0")
                            .withVertices(
                                    anInitialPseudoStateCalled("initial"),
                                    aSimpleStateCalled("state1"),
                                    aSimpleStateCalled("state2"),
                                    aFinalStateCalled("final"))
                            .withTransitions(
                                    anExternalTransitionCalled("_trigger1").startingAt("initial").endingAt("state1"),
                                    anExternalTransitionCalled("_trigger2").startingAt("state1").endingAt("state2").triggeredBy(anEventCalled("realEvent")),
                                    anExternalTransitionCalled("_trigger3").startingAt("state2").endingAt("final")
                            )
            )


    fun fsmWithTwoOutcomesPrototype() =
            aStateMachineCalled("context").withRegion(
                    RegionBuilder.aRegionCalled("region0")
                            .withVertices(
                                    anInitialPseudoStateCalled("start"),
                                    aSimpleStateCalled("middle"),
                                    aSimpleStateCalled("left"),
                                    aSimpleStateCalled("right"),
                                    aFinalStateCalled("end"))
                            .withTransitions(
                                    anExternalTransitionCalled("_trigger1").startingAt("start").endingAt("middle"),
                                    anExternalTransitionCalled("_trigger2").startingAt("middle").endingAt("left").triggeredBy(anEventCalled("goLeft")),
                                    anExternalTransitionCalled("_trigger3").startingAt("middle").endingAt("right").triggeredBy(anEventCalled("goRight")),
                                    anExternalTransitionCalled("_trigger4").startingAt("left").endingAt("end").triggeredBy(anEventCalled("finish")),
                                    anExternalTransitionCalled("_trigger5").startingAt("right").endingAt("end").triggeredBy(anEventCalled("finish"))
                            )
            )


    fun fsmWithDeferredTransitionsPrototype() =
            aStateMachineCalled("context").withRegion(
                    aRegionCalled("region0")
                            .withVertices(
                                    anInitialPseudoStateCalled("initial"),
                                    aSimpleStateCalled("state1")
                                            .withDeferrableTriggers(
                                                    aTriggerCalled("State1_DE1").firedWith(anEventCalled("deferredEvent1")),
                                                    aTriggerCalled("State1_DE3").firedWith(anEventCalled("deferredEvent2"))),
                                    aSimpleStateCalled("state2")
                                            .withDeferrableTriggers(
                                                    aTriggerCalled("State2_DE1").firedWith(anEventCalled("deferredEvent1")),
                                                    aTriggerCalled("State2_DE3").firedWith(anEventCalled("deferredEvent2"))),
                                    aSimpleStateCalled("state3")
                                            .withDeferrableTriggers(
                                                    aTriggerCalled("State3_DE1").firedWith(anEventCalled("deferredEvent2"))),
                                    aSimpleStateCalled("state4"),
                                    aFinalStateCalled("final")
                            )
                            .withTransitions(
                                    anExternalTransitionCalled("region0_trigger1").startingAt("initial").endingAt("state1"),
                                    anExternalTransitionCalled("region0_trigger2").startingAt("state1").endingAt("state2").triggeredBy(anEventCalled("realEvent")),
                                    anExternalTransitionCalled("region0_trigger3").startingAt("state2").endingAt("state3"),
                                    anExternalTransitionCalled("region0_trigger4").startingAt("state3").endingAt("state4").triggeredBy(anEventCalled("deferredEvent1")),
                                    anExternalTransitionCalled("region0_trigger5").startingAt("state4").endingAt("final").triggeredBy(anEventCalled("deferredEvent2"))
                            )
            )

    fun fsmWithDeferredAndAutomatedTransitionsPrototype() =
            aStateMachineCalled("context").withRegion(
                    aRegionCalled("region0")
                            .withVertices(
                                    anInitialPseudoStateCalled("initial"),
                                    aSimpleStateCalled("state1")
                                            .withDeferrableTriggers(
                                                    aTriggerCalled("S1_DT1").firedWith(anEventCalled("deferredEvent1")),
                                                    aTriggerCalled("S1_DT2").firedWith(anEventCalled("deferredEvent2"))),
                                    aSimpleStateCalled("state2")
                                            .withDeferrableTriggers(
                                                    aTriggerCalled("S2_DT1").firedWith(anEventCalled("deferredEvent1")),
                                                    aTriggerCalled("S2_DT2").firedWith(anEventCalled("deferredEvent2"))
                                            ),
                                    aSimpleStateCalled("state3"),
                                    aSimpleStateCalled("state4"),
                                    aSimpleStateCalled("state5"),
                                    aFinalStateCalled("final")
                            )
                            .withTransitions(
                                    anExternalTransitionCalled("T1").startingAt("initial").endingAt("state1"),
                                    anExternalTransitionCalled("T2").startingAt("state1").endingAt("state2").triggeredBy(anEventCalled("realEvent")),
                                    anExternalTransitionCalled("T3").startingAt("state2").endingAt("state3").triggeredBy(anEventCalled("deferredEvent1")),
                                    anExternalTransitionCalled("T4").startingAt("state2").endingAt("state4"),
                                    anExternalTransitionCalled("T5").startingAt("state2").endingAt("state5").triggeredBy(anEventCalled("deferredEvent2")),
                                    anExternalTransitionCalled("T6").startingAt("state4").endingAt("state3").triggeredBy(anEventCalled("deferredEvent1")),
                                    anExternalTransitionCalled("T7").startingAt("state4").endingAt("state5").triggeredBy(anEventCalled("deferredEvent2")),
                                    anExternalTransitionCalled("T8").startingAt("state4").endingAt("final").triggeredBy(anEventCalled("otherEvent")),
                                    anExternalTransitionCalled("T9").startingAt("state5").endingAt("final").triggeredBy(anEventCalled("deferredEvent1"))
                            )
            )


    fun fsmWithEntryAndExitBehaviorsPrototype() =
            aStateMachineCalled("context").withRegion(
                    aRegionCalled("region0")
                            .withVertices(
                                    anInitialPseudoStateCalled("initial"),
                                    aSimpleStateCalled("state1")
                                            .withEntryBehavior(aBehaviorCalled("doLoggingEntryAction()").withAction { log.debug("entry action for $it") })
                                            .withExitBehavior(aBehaviorCalled("doLoggingExitAction()").withAction { log.debug("exit action for $it") }),
                                    aSimpleStateCalled("state2"),
                                    aSimpleStateCalled("state3")
                            )
                            .withTransitions(
                                    anExternalTransitionCalled("region0_trans1").startingAt("initial").endingAt("state1").triggeredBy(anEventCalled("e1")),
                                    anExternalTransitionCalled("region0_trans2").startingAt("state1").endingAt("state2").triggeredBy(anEventCalled("internalEven_trigger1")),
                                    anExternalTransitionCalled("region0_trans3").startingAt("state1").endingAt("state3").triggeredBy(anEventCalled("internalEven_trigger2"))
                            )
            )


    fun nestedStateStateMachinePrototype() =
            aStateMachineCalled("context").withRegion(
                    aRegionCalled("region0")
                            .withVertices(
                                    anInitialPseudoStateCalled("initial"),
                                    aSimpleStateCalled("state1"),
                                    aSimpleStateCalled("state2"),
                                    aCompositeStateCalled("state3"),
                                    aFinalStateCalled("final")
                            )
                            .withTransitions(
                                    anExternalTransitionCalled("trans1").startingAt("initial").endingAt("state1"),
                                    anExternalTransitionCalled("trans2").startingAt("state1").endingAt("state2").triggeredBy(anEventCalled("succeeded")),
                                    anExternalTransitionCalled("trans3").startingAt("state1").endingAt("final").triggeredBy(anEventCalled("aborted")),
                                    anExternalTransitionCalled("trans4").startingAt("state2").endingAt("final").triggeredBy(anEventCalled("aborted")),
                                    anExternalTransitionCalled("trans5").startingAt("state2").endingAt("state3").triggeredBy(anEventCalled("succeeded")),
                                    anExternalTransitionCalled("trans6").startingAt("state3").endingAt("final").triggeredBy(anEventCalled("succeeded")),
                                    anExternalTransitionCalled("trans7").startingAt("state3").endingAt("final").triggeredBy(anEventCalled("aborted")),
                                    anExternalTransitionCalled("trans8").startingAt("state3").endingAt("state3").triggeredBy(anEventCalled("failed"))
                            )

            )

}