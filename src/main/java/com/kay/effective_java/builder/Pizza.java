package com.kay.effective_java.builder;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public abstract class Pizza {

    final Set<Topping> toppings;

    Pizza(AbstractBuilder<?> builder) {
        toppings = builder.toppings.clone();
    }

    public enum Topping {HAM, MUSHROOM, ONION, PEPPER, SAUSAGE}

    abstract static class AbstractBuilder<T extends AbstractBuilder<T>> {

        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addToppings(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        abstract Pizza build();

        protected abstract T self();
    }

}
