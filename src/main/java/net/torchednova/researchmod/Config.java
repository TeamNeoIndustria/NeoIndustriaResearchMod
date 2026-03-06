package net.torchednova.researchmod;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


    public static final ModConfigSpec.IntValue TimeForVote = BUILDER
            .comment("Max Time for a vote in mins. Min: 5 Max: int32 max")
            .defineInRange("TimeForVote", 1440, 5, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue LowerTimeForVote = BUILDER
            .comment("Min Time for a vote in mins after x amount of votes. Min: 5 Max: int32 max")
            .defineInRange("LowerTimeForVote", 60, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue VotesToLowerTime = BUILDER
            .comment("The Amount of votes to lower time for vote. Min: 1 Max: int32 max")
            .defineInRange("VotesToLowerTime", 5, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue timetoCheckDone = BUILDER
            .comment("The Amount of time in seconds between checks if research is done. Min: 1 Max: int32 max")
            .defineInRange("timetocheckdone", 10, 1, Integer.MAX_VALUE);



    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
