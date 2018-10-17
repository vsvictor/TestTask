package com.education.middle.data;

public enum RowStatus { LOADED, ERROR, UNDEFINED;

    public int toInt() {
        if(this == LOADED) return 1;
        else if(this == ERROR) return 2;
        else return 3;
    }
}
