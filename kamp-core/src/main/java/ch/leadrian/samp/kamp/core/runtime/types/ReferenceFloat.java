package ch.leadrian.samp.kamp.core.runtime.types;

public class ReferenceFloat {

    @SuppressWarnings("WeakerAccess")
    float value = 0f;

    public void setValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

}
