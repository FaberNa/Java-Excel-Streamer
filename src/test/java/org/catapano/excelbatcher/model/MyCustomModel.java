package org.catapano.excelbatcher.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class MyCustomModel {

    private LocalDate date;
    private BigDecimal amount;
    private int quantity;
    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    // ======= costruttore privato (builder only) =======
    private MyCustomModel() {
    }

    // ======= getter =======
    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isActive() {
        return active;
    }

    // ======= builder =======
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final MyCustomModel target;

        private Builder() {
            this.target = new MyCustomModel();
        }

        public Builder date(LocalDate date) {
            target.date = date;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            target.amount = amount;
            return this;
        }

        public Builder quantity(int quantity) {
            target.quantity = quantity;
            return this;
        }

        public Builder active(boolean active) {
            target.active = active;
            return this;
        }


        public Builder name(String name) {
            target.name = name;
            return this;
        }

        public MyCustomModel build() {
            return target;
        }
    }

    // ======= equals / hashCode =======
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MyCustomModel that)) return false;
        return quantity == that.quantity
                && active == that.active
                && Objects.equals(date, that.date)
                && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount, quantity, active);
    }

    // ======= toString =======
    @Override
    public String toString() {
        return "MyCustomModel{" +
                "date=" + date +
                ", amount=" + amount +
                ", quantity=" + quantity +
                ", active=" + active +
                '}';
    }
}