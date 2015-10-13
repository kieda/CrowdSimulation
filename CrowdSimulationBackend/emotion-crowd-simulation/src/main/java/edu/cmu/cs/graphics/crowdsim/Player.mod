import 
    edu.cmu.cs.graphics.crowdsim.ai.core.*
    edu.cmu.cs.graphics.crowdsim.ai.core.internal.*
    edu.cmu.cs.graphics.crowdsim.ai.core.action.*
    edu.cmu.cs.graphics.crowdsim.ai.core.perception.*
end
Player : 
has
    GameModule :
    has
        GameModule$GroupModule
        GameModule$ObjectiveModule
    end

    TokenModule
    ActionModule : 
    has
        AttackModule
        FaceChangeModule
        MovementModule
    end

    PerceptionModule : 
    has 
        PerceptionEmotionChange
    end

    InternalModule : 
    has
        MoodColorModule
        EmotionLinearSystem : BasicEmotionGraphConstructor
    end
end