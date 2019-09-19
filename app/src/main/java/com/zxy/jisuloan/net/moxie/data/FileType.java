package com.zxy.jisuloan.net.moxie.data;

public enum FileType {
    OCRFRONT(1),OCRBACK(2),LIVENESS(3);

    int type;
    FileType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
