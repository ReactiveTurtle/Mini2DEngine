package ru.reactiveturtle.mini2dengine;

class PhysicalObject extends GameObject {
    private boolean isGravityEnabled = false;
    private int x, y;
    private float gravity;

    public void enableGravity(Gravity gravity, int x, int y) {
        this.gravity = gravity.getValue();
        initGravity(x, y);
    }

    public void enableGravity(float gravity, int x, int y) {
        this.gravity = gravity;
        initGravity(x, y);
    }

    private void initGravity(int x, int y) {
        isGravityEnabled = true;
        this.x = x;
        this.y = y;
    }

    public void disableGravity() {
        isGravityEnabled = false;
    }

    public boolean isGravityEnabled() {
        return isGravityEnabled;
    }

    void showChanges(long lastTime) {

    }

    public enum Gravity {
        EARTH(9.81f);

        private float value;

        Gravity(float v) {
            value = v;
        }

        public float getValue() {
            return value;
        }
    }
}
