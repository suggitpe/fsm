package org.suggs.fsm.engine.uml2.optimisation

import org.easymock.MockControl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.suggs.fsm.uml2.basicbehaviors.IBehavior
import org.suggs.fsm.uml2.basicbehaviors.IBehavioredClassifier
import org.suggs.fsm.uml2.behaviorstatemachines.IPseudoState
import org.suggs.fsm.uml2.behaviorstatemachines.IRegion
import org.suggs.fsm.uml2.behaviorstatemachines.IState
import org.suggs.fsm.uml2.behaviorstatemachines.IStateMachine
import org.suggs.fsm.uml2.scribe.optimisation.IModelOptimiser

class ModelOptimiserTest {

    private lateinit var modelOptimiser: IModelOptimiser

    @Mock lateinit var behavior: IBehavior
    @Mock lateinit var behavioredClassifier: IBehavioredClassifier
    @Mock lateinit var region: IRegion
    @Mock lateinit var simpleState: IState
    @Mock lateinit var simpleCompositeState: IState
    @Mock lateinit var shallowHistoryState: IPseudoState
    @Mock lateinit var deepHistoryState: IPseudoState
    @Mock lateinit var initialState: IPseudoState
    @Mock lateinit var entryPoint: IPseudoState
    @Mock lateinit var exitPoint: IPseudoState
    @Mock lateinit var stateMachine: IStateMachine

    @BeforeEach fun setUp() {
        modelOptimiser = ModelOptimiser()
        initMocks(this)
    }

    /**
     * Just tests that the optimised calls the right objects. */
    @Test
    fun testOptimisationCallbacks() {

        modelOptimiser.optimiseBehavior(behavior)
        verify(behavior).acceptOptimiser(modelOptimiser)

        modelOptimiser.optimiseBehavioredClassifier(behavioredClassifier)
        verify(behavioredClassifier).acceptOptimiser(modelOptimiser)

        modelOptimiser.optimiseDeepHistoryState(deepHistoryState)
        verify(deepHistoryState).acceptOptimiser(modelOptimiser)

        modelOptimiser.optimiseEntryPointPseudoState(entryPoint)
        verify(entryPoint).acceptOptimiser(modelOptimiser)

        modelOptimiser.optimiseExitPointPseudoState(exitPoint)
        verify(exitPoint).acceptOptimiser(modelOptimiser)

        modelOptimiser.optimiseInitialPseudoState(initialState)
        verify(initialState).acceptOptimiser(modelOptimiser)

        modelOptimiser.optimiseRegion(region)
        verify(region).acceptOptimiser(modelOptimiser)

        modelOptimiser.optimiseShallowHistoryState(shallowHistoryState)
        verify(shallowHistoryState).acceptOptimiser(modelOptimiser)

        modelOptimiser.optimiseSimpleCompositeState(simpleCompositeState)
        verify(simpleCompositeState).acceptOptimiser(modelOptimiser)

        modelOptimiser.optimiseSimpleState(simpleState)
        verify(simpleState).acceptOptimiser(modelOptimiser)

        modelOptimiser.optimiseStateMachine(stateMachine)
        verify(stateMachine).acceptOptimiser(modelOptimiser)
    }
}