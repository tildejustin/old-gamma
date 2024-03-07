package dev.tildejustin.old_gamma;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.client.option.SimpleOption;

import java.util.*;
import java.util.function.*;

public enum DoubleSliderCallbacksGamma implements SimpleOption.SliderCallbacks<Double> {
    INSTANCE;

    public Optional<Double> validate(Double d) {
        return d >= 0.0 && d <= 5.0 ? Optional.of(d) : Optional.empty();
    }

    public double toSliderProgress(Double d) {
        return d / 5;
    }

    public Double toValue(double d) {
        return d * 5;
    }

    @SuppressWarnings("unused")
    public <R> SimpleOption.SliderCallbacks<R> withModifier(
            DoubleFunction<? extends R> sliderProgressValueToValue,
            ToDoubleFunction<? super R> valueToSliderProgressValue
    ) {
        return new SimpleOption.SliderCallbacks<R>() {
            @Override
            public Optional<R> validate(R value) {
                return DoubleSliderCallbacksGamma.this.validate(valueToSliderProgressValue.applyAsDouble(value)).map(sliderProgressValueToValue::apply);
            }

            @Override
            public double toSliderProgress(R value) {
                return DoubleSliderCallbacksGamma.this.toSliderProgress(valueToSliderProgressValue.applyAsDouble(value));
            }

            @Override
            public R toValue(double sliderProgress) {
                return sliderProgressValueToValue.apply(DoubleSliderCallbacksGamma.this.toValue(sliderProgress));
            }

            @Override
            public Codec<R> codec() {
                return DoubleSliderCallbacksGamma.this.codec().xmap(sliderProgressValueToValue::apply, valueToSliderProgressValue::applyAsDouble);
            }
        };
    }

    @Override
    public Codec<Double> codec() {
        return Codec.either(Codec.doubleRange(0.0, 5.0), Codec.BOOL)
                .xmap(either -> either.map(value -> value, value -> value ? 1.0 : 0.0), Either::left);
    }
}
