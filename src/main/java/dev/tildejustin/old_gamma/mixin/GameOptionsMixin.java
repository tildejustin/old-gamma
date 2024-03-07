package dev.tildejustin.old_gamma.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import dev.tildejustin.old_gamma.DoubleSliderCallbacksGamma;
import net.minecraft.client.option.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
    @Shadow
    public static Text getGenericValueText(Text prefix, Text value) {
        return null;
    }

    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/option/SimpleOption;<init>(Ljava/lang/String;Lnet/minecraft/client/option/SimpleOption$TooltipFactory;Lnet/minecraft/client/option/SimpleOption$ValueTextGetter;Lnet/minecraft/client/option/SimpleOption$Callbacks;Ljava/lang/Object;Ljava/util/function/Consumer;)V",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "CONSTANT",
                            args = "stringValue=options.gamma"
                    )
            ),
            index = 3
    )
    private SimpleOption.Callbacks<?> replaceGammaSliderCallback(SimpleOption.Callbacks<?> original) {
        return DoubleSliderCallbacksGamma.INSTANCE;
    }

    @WrapOperation(method = "method_42492", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;getGenericValueText(Lnet/minecraft/text/Text;I)Lnet/minecraft/text/Text;"))
    private static Text addMaxGammaText(Text prefix, int value, Operation<Text> original) {
        if (value == 500) {
            return getGenericValueText(prefix, Text.translatable("options.ao.max"));
        }
        return original.call(prefix, value);
    }
}
