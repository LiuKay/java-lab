package com.kay.effective_java.builder;

import java.util.Objects;

public class NyPizza extends Pizza {

		public enum Size {SMALL, MEDIUM, LARGE}

		private final Size size;

		private NyPizza(Builder builder) {
				super(builder);
				size = builder.size;
		}

		public static class Builder extends AbstractBuilder<Builder> {

				private final Size size;

				public Builder(Size size) {
						this.size = Objects.requireNonNull(size);
				}

				@Override
				public NyPizza build() {
						return new NyPizza(this);
				}

				@Override
				protected Builder self() {
						return this;
				}
		}


		public static void main(String[] args) {
				NyPizza nyPizza = new Builder(Size.LARGE)
						.addToppings(Topping.HAM)
						.addToppings(Topping.ONION)
						.build();

				Calzone calzone = new Calzone.Builder()
						.addToppings(Topping.MUSHROOM)
						.addToppings(Topping.PEPPER)
						.sauceInside().build();

		}
}
