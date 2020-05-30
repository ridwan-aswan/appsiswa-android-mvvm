package com.ridwanrsup94atgmaildotcom.appsiswa.model;

import com.google.gson.annotations.SerializedName;
import com.ridwanrsup94atgmaildotcom.appsiswa.helper.Const;

import java.io.Serializable;
/**
 * Created by RIDWAN on 26/05/20.
 * ridwanrsup94@gmail.com
 */
public class ServerResponse implements Serializable {

    @SerializedName(Const.KEY_MESSAGE)
    String message;

    @SerializedName(Const.KEY_CODE)
    int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
