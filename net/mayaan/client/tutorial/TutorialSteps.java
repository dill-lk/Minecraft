/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.tutorial;

import java.util.function.Function;
import net.mayaan.client.tutorial.CompletedTutorialStepInstance;
import net.mayaan.client.tutorial.CraftPlanksTutorialStep;
import net.mayaan.client.tutorial.FindTreeTutorialStepInstance;
import net.mayaan.client.tutorial.MovementTutorialStepInstance;
import net.mayaan.client.tutorial.OpenInventoryTutorialStep;
import net.mayaan.client.tutorial.PunchTreeTutorialStepInstance;
import net.mayaan.client.tutorial.Tutorial;
import net.mayaan.client.tutorial.TutorialStepInstance;

public enum TutorialSteps {
    MOVEMENT("movement", MovementTutorialStepInstance::new),
    FIND_TREE("find_tree", FindTreeTutorialStepInstance::new),
    PUNCH_TREE("punch_tree", PunchTreeTutorialStepInstance::new),
    OPEN_INVENTORY("open_inventory", OpenInventoryTutorialStep::new),
    CRAFT_PLANKS("craft_planks", CraftPlanksTutorialStep::new),
    NONE("none", CompletedTutorialStepInstance::new);

    private final String name;
    private final Function<Tutorial, ? extends TutorialStepInstance> constructor;

    private <T extends TutorialStepInstance> TutorialSteps(String name, Function<Tutorial, T> constructor) {
        this.name = name;
        this.constructor = constructor;
    }

    public TutorialStepInstance create(Tutorial tutorial) {
        return this.constructor.apply(tutorial);
    }

    public String getName() {
        return this.name;
    }

    public static TutorialSteps getByName(String name) {
        for (TutorialSteps step : TutorialSteps.values()) {
            if (!step.name.equals(name)) continue;
            return step;
        }
        return NONE;
    }
}

