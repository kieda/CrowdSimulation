import 
    edu.cmu.cs464.p3.ai.core.*
    edu.cmu.cs464.p3.ai.core.internal.*
    edu.cmu.cs464.p3.ai.core.action.*
    edu.cmu.cs464.p3.ai.core.perception.*
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