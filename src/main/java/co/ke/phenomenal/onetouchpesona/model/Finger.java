package co.ke.phenomenal.onetouchpesona.model;

import com.digitalpersona.onetouch.DPFPFingerIndex;

public enum Finger {
    RightThumb(DPFPFingerIndex.RIGHT_THUMB){
        @Override
        public Finger next() {
            return Finger.RightIndex;
        }
    },
    RightIndex(DPFPFingerIndex.RIGHT_INDEX) {
        @Override
        public Finger next() {
            return Finger.RightMiddle;
        }
    },
    RightMiddle(DPFPFingerIndex.RIGHT_MIDDLE) {
        @Override
        public Finger next() {
            return Finger.RightRing;
        }
    },
    RightRing(DPFPFingerIndex.RIGHT_RING) {
        @Override
        public Finger next() {
            return Finger.RightLittle;
        }
    },
    RightLittle(DPFPFingerIndex.RIGHT_PINKY) {
        @Override
        public Finger next() {
            return Finger.LeftThumb;
        }
    },
    LeftThumb(DPFPFingerIndex.LEFT_THUMB) {
        @Override
        public Finger next() {
            return Finger.LeftIndex;
        }
    },
    LeftIndex(DPFPFingerIndex.LEFT_INDEX) {
        @Override
        public Finger next() {
            return Finger.LeftMiddle;
        }
    },
    LeftMiddle(DPFPFingerIndex.LEFT_MIDDLE) {
        @Override
        public Finger next() {
            return Finger.LeftRing;
        }
    },
    LeftRing(DPFPFingerIndex.LEFT_RING) {
        @Override
        public Finger next() {
            return Finger.LeftLittle;
        }
    },
    LeftLittle(DPFPFingerIndex.LEFT_PINKY) {
        @Override
        public Finger next() {
            return null;
        }
    };
    private final  DPFPFingerIndex fingerIndex;
    Finger(DPFPFingerIndex fingerIndex) {
        this.fingerIndex = fingerIndex;
    }

    public DPFPFingerIndex getFingerIndex() {
        return fingerIndex;
    }

    public abstract Finger next();
}
