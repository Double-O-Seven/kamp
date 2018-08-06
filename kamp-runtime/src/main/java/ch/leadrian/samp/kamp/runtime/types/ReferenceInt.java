package ch.leadrian.samp.kamp.runtime.types;

public class ReferenceInt {

    @SuppressWarnings("WeakerAccess")
    int value = 0;

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
